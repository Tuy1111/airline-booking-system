package com.abs.booking.application.usecase;

import com.abs.booking.application.BookingEventPublisher;
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
public class HandlePaymentFailedUseCase {

    private final BookingRepository bookingRepository;
    private final BookingEventPublisher eventPublisher;
    private final FlightSearchClient flightSearchClient;
    private final UserServiceClient userServiceClient;
    private final SeatLockService seatLockService;

    @Transactional
    public void execute(Long bookingId, String reason) {
        BookingAggregate booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            log.info("Booking {} is already CANCELLED. Skipping duplicate event processing.", bookingId);
            return;
        }

        // Delegate state transition to domain aggregate
        booking.cancel();

        // Release Redis seat locks and flight search seats
        final Long flightId = booking.getFlightId();
        booking.getSeatNumbers().forEach(seatNo -> {
            seatLockService.releaseLock(flightId, seatNo);
            flightSearchClient.releaseSeat(flightId, seatNo);
        });

        booking = bookingRepository.save(booking);

        // Enrich details for event
        String email = userServiceClient.getUserEmail(booking.getUserId());
        String passengerName = (booking.getItems() == null || booking.getItems().isEmpty())
                ? "Passenger"
                : booking.getItems().get(0).getPassengerName();

        // Publish event via outbox
        eventPublisher.publishCancelled(booking, email, passengerName, reason);

        log.info("Booking cancelled due to payment failure: bookingCode={}, reason={}",
                booking.getBookingCode(), reason);
    }
}
