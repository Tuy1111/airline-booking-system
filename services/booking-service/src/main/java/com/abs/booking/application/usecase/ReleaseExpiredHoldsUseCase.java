package com.abs.booking.application.usecase;

import com.abs.booking.application.BookingEventPublisher;
import com.abs.booking.domain.aggregate.BookingAggregate;
import com.abs.booking.domain.vo.BookingStatus;
import com.abs.booking.domain.repository.BookingRepository;
import com.abs.booking.infrastructure.client.FlightSearchClient;
import com.abs.booking.infrastructure.redis.SeatLockService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ReleaseExpiredHoldsUseCase {

    private final BookingRepository bookingRepository;
    private final BookingEventPublisher eventPublisher;
    private final FlightSearchClient flightSearchClient;
    private final SeatLockService seatLockService;
    private final Counter expiredCounter;

    public ReleaseExpiredHoldsUseCase(BookingRepository bookingRepository,
                                     BookingEventPublisher eventPublisher,
                                     FlightSearchClient flightSearchClient,
                                     SeatLockService seatLockService,
                                     MeterRegistry meterRegistry) {
        this.bookingRepository = bookingRepository;
        this.eventPublisher = eventPublisher;
        this.flightSearchClient = flightSearchClient;
        this.seatLockService = seatLockService;
        this.expiredCounter = Counter.builder("booking.expired")
                .description("Number of bookings expired")
                .register(meterRegistry);
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void execute() {
        List<BookingAggregate> expiredBookings = bookingRepository.findExpiredHolds(
                BookingStatus.HELD, LocalDateTime.now());

        if (expiredBookings.isEmpty()) {
            return;
        }

        for (BookingAggregate booking : expiredBookings) {
            // Delegate state transition to domain aggregate
            booking.expire();

            // Release Redis seat locks and flight search seats
            final Long flightId = booking.getFlightId();
            booking.getSeatNumbers().forEach(seatNo -> {
                seatLockService.releaseLock(flightId, seatNo);
                flightSearchClient.releaseSeat(flightId, seatNo);
            });

            bookingRepository.save(booking);
            eventPublisher.publishExpired(booking);
        }

        expiredCounter.increment(expiredBookings.size());
        log.info("Released {} expired booking holds", expiredBookings.size());
    }
}
