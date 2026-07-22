package com.abs.booking.application.usecase;

import com.abs.booking.application.BookingEventPublisher;
import com.abs.booking.application.dto.FlightDetailResponse;
import com.abs.booking.domain.aggregate.BookingAggregate;
import com.abs.booking.domain.vo.BookingStatus;
import com.abs.booking.domain.exception.BookingNotFoundException;
import com.abs.booking.domain.repository.BookingRepository;
import com.abs.booking.infrastructure.client.FlightSearchClient;
import com.abs.booking.infrastructure.client.UserServiceClient;
import com.abs.booking.infrastructure.redis.SeatLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmBookingUseCase {

    private final BookingRepository bookingRepository;
    private final BookingEventPublisher eventPublisher;
    private final FlightSearchClient flightSearchClient;
    private final UserServiceClient userServiceClient;
    private final SeatLockService seatLockService;

    @Transactional
    public void execute(Long bookingId, String paymentId) {
        BookingAggregate booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            log.info("Booking {} is already CONFIRMED. Skipping duplicate event processing.", bookingId);
            return;
        }

        // Delegate state transition to domain aggregate
        booking.confirm(paymentId);

        // Release Redis seat locks and book seats in flight search
        final Long flightId = booking.getFlightId();
        booking.getSeatNumbers().forEach(seatNo -> {
            seatLockService.releaseLock(flightId, seatNo);
            boolean bookSuccess = flightSearchClient.bookSeat(flightId, seatNo);
            if (!bookSuccess) {
                log.error("Failed to book seat {} for flightId {} in flight-search-service during confirmation", seatNo,
                        flightId);
                throw new IllegalStateException("Không thể xác nhận ghế " + seatNo + " trên hệ thống chuyến bay");
            }
        });

        booking = bookingRepository.save(booking);

        // Enrich email and flight info
        String email = userServiceClient.getUserEmail(booking.getUserId());
        String passengerName = (booking.getItems() == null || booking.getItems().isEmpty())
                ? "Passenger"
                : booking.getItems().get(0).getPassengerName();

        FlightDetailResponse flightInfo = flightSearchClient.getFlightDetails(booking.getFlightId());
        String flightNo = flightInfo != null ? flightInfo.flightNo() : "Unknown";
        String from = flightInfo != null ? flightInfo.fromAirport() : "Unknown";
        String to = flightInfo != null ? flightInfo.toAirport() : "Unknown";
        String departureTime = flightInfo != null ? String.valueOf(flightInfo.departureTime()) : "Unknown";

        // Publish event via outbox
        eventPublisher.publishConfirmed(booking, email, passengerName, flightNo, from, to, departureTime);

        log.info("Booking confirmed and event published: bookingCode={}, paymentId={}",
                booking.getBookingCode(), paymentId);
    }
}
