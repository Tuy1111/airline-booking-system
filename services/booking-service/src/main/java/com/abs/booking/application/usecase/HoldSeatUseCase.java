package com.abs.booking.application.usecase;

import com.abs.booking.application.BookingEventPublisher;
import com.abs.booking.application.dto.HoldSeatRequest;
import com.abs.booking.application.dto.HoldSeatResponse;
import com.abs.booking.application.dto.SeatInfoResponse;
import com.abs.booking.domain.aggregate.BookingAggregate;
import com.abs.booking.domain.aggregate.BookingItem;
import com.abs.booking.domain.exception.SeatAlreadyHeldException;
import com.abs.booking.domain.exception.SeatNotAvailableException;
import com.abs.booking.domain.repository.BookingRepository;
import com.abs.booking.infrastructure.client.FlightSearchClient;
import com.abs.booking.infrastructure.redis.SeatLockService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
public class HoldSeatUseCase {

    private final BookingRepository bookingRepository;
    private final BookingEventPublisher eventPublisher;
    private final FlightSearchClient flightSearchClient;
    private final SeatLockService seatLockService;
    private final Counter heldCounter;
    private final int holdTtlMinutes;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public HoldSeatUseCase(BookingRepository bookingRepository,
                           BookingEventPublisher eventPublisher,
                           FlightSearchClient flightSearchClient,
                           SeatLockService seatLockService,
                           MeterRegistry meterRegistry,
                           @Value("${booking.hold.ttl-minutes:10}") int holdTtlMinutes) {
        this.bookingRepository = bookingRepository;
        this.eventPublisher = eventPublisher;
        this.flightSearchClient = flightSearchClient;
        this.seatLockService = seatLockService;
        this.holdTtlMinutes = holdTtlMinutes;
        this.heldCounter = Counter.builder("booking.held")
                .description("Number of bookings held")
                .register(meterRegistry);
    }

    @Transactional
    public HoldSeatResponse execute(HoldSeatRequest req, Long userId) {
        // Generate booking code
        String bookingCode = "BK" + LocalDate.now().format(DATE_FORMATTER)
                + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        // Call flight-search-client to check seat availability
        SeatInfoResponse seatInfo = flightSearchClient.checkSeat(req.flightId(), req.seatNo());

        if (seatInfo == null || !"AVAILABLE".equals(seatInfo.status())) {
            throw new SeatNotAvailableException(req.flightId(), req.seatNo());
        }

        BigDecimal seatPrice = seatInfo.price();
        int baggageKg = (req.extraBaggageKg() != null) ? req.extraBaggageKg() : 0;
        if (baggageKg < 0 || baggageKg > 20) {
            throw new IllegalArgumentException("Hành lý ký gửi phải từ 0 đến tối đa 20kg");
        }
        BigDecimal baggageFee = BigDecimal.valueOf(baggageKg * 1000L);
        BigDecimal totalAmount = seatPrice.add(baggageFee);

        // Redis distributed lock
        boolean acquired = seatLockService.acquireLock(
                req.flightId(), req.seatNo(), userId, Duration.ofMinutes(holdTtlMinutes));

        if (!acquired) {
            throw new SeatAlreadyHeldException(req.flightId(), req.seatNo());
        }

        // Call flight-search-client to hold seat in database
        boolean holdSuccess = flightSearchClient.holdSeat(req.flightId(), req.seatNo());
        if (!holdSuccess) {
            seatLockService.releaseLock(req.flightId(), req.seatNo());
            throw new SeatNotAvailableException(req.flightId(), req.seatNo());
        }

        // Build using aggregate factory and save booking
        BookingAggregate booking = BookingAggregate.createHold(bookingCode, userId, req.flightId(), totalAmount,
                baggageKg, baggageFee, holdTtlMinutes);

        BookingItem item = BookingItem.builder()
                .seatNo(req.seatNo())
                .passengerName(req.passengerName())
                .passengerPassport(req.passengerPassport())
                .price(seatPrice)
                .build();

        booking.addItem(item);
        booking = bookingRepository.save(booking);

        // Publish event via outbox
        eventPublisher.publishHeld(booking);

        // Increment metric
        heldCounter.increment();

        log.info("Seat held successfully: bookingCode={}, flightId={}, seatNo={}, baggageKg={}, baggageFee={}, totalAmount={}",
                bookingCode, req.flightId(), req.seatNo(), baggageKg, baggageFee, totalAmount);

        return HoldSeatResponse.of(
                booking.getId(),
                booking.getBookingCode(),
                booking.getFlightId(),
                req.seatNo(),
                totalAmount,
                baggageKg,
                baggageFee,
                booking.getCurrency(),
                booking.getExpiresAt());
    }
}
