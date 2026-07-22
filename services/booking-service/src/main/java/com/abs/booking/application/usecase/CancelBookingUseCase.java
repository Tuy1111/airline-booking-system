package com.abs.booking.application.usecase;

import com.abs.booking.application.BookingEventPublisher;
import com.abs.booking.application.dto.BookingDetailResponse;
import com.abs.booking.domain.aggregate.BookingAggregate;
import com.abs.booking.domain.exception.BookingNotFoundException;
import com.abs.booking.domain.exception.UnauthorizedBookingAccessException;
import com.abs.booking.domain.repository.BookingRepository;
import com.abs.booking.infrastructure.client.FlightSearchClient;
import com.abs.booking.infrastructure.client.UserServiceClient;
import com.abs.booking.infrastructure.redis.SeatLockService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CancelBookingUseCase {

    private final BookingRepository bookingRepository;
    private final BookingEventPublisher eventPublisher;
    private final FlightSearchClient flightSearchClient;
    private final UserServiceClient userServiceClient;
    private final SeatLockService seatLockService;
    private final Counter cancelledCounter;

    public CancelBookingUseCase(BookingRepository bookingRepository,
                                BookingEventPublisher eventPublisher,
                                FlightSearchClient flightSearchClient,
                                UserServiceClient userServiceClient,
                                SeatLockService seatLockService,
                                MeterRegistry meterRegistry) {
        this.bookingRepository = bookingRepository;
        this.eventPublisher = eventPublisher;
        this.flightSearchClient = flightSearchClient;
        this.userServiceClient = userServiceClient;
        this.seatLockService = seatLockService;
        this.cancelledCounter = Counter.builder("booking.cancelled")
                .description("Number of bookings cancelled")
                .register(meterRegistry);
    }

    @Transactional
    public BookingDetailResponse execute(Long id, Long userId) {
        BookingAggregate booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        if (!booking.getUserId().equals(userId)) {
            throw new UnauthorizedBookingAccessException(userId, id);
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

        // Enrich and publish event via outbox
        String email = userServiceClient.getUserEmail(booking.getUserId());
        String passengerName = (booking.getItems() == null || booking.getItems().isEmpty())
                ? "Passenger"
                : booking.getItems().get(0).getPassengerName();
        eventPublisher.publishCancelled(booking, email, passengerName, "Cancelled by user");

        // Increment metric
        cancelledCounter.increment();

        log.info("Booking cancelled: bookingCode={}", booking.getBookingCode());

        return BookingDetailResponse.of(booking);
    }
}
