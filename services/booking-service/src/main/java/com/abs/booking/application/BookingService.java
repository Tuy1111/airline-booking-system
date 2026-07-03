package com.abs.booking.application;

import com.abs.booking.application.dto.BookingDetailResponse;
import com.abs.booking.application.dto.HoldSeatRequest;
import com.abs.booking.application.dto.HoldSeatResponse;
import com.abs.booking.domain.aggregate.BookingAggregate;
import com.abs.booking.domain.aggregate.BookingItem;
import com.abs.booking.domain.vo.BookingStatus;
import com.abs.booking.domain.repository.BookingRepository;
import com.abs.booking.domain.exception.*;
import com.abs.booking.infrastructure.client.FlightSearchClient;
import com.abs.booking.infrastructure.client.UserServiceClient;
import com.abs.booking.infrastructure.redis.SeatLockService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingEventPublisher eventPublisher;
    private final FlightSearchClient flightSearchClient;
    private final UserServiceClient userServiceClient;
    private final SeatLockService seatLockService;
    private final Counter heldCounter;
    private final Counter cancelledCounter;
    private final Counter expiredCounter;
    private final int holdTtlMinutes;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public BookingService(BookingRepository bookingRepository,
                          BookingEventPublisher eventPublisher,
                          FlightSearchClient flightSearchClient,
                          UserServiceClient userServiceClient,
                          SeatLockService seatLockService,
                          MeterRegistry meterRegistry,
                          @Value("${booking.hold.ttl-minutes:10}") int holdTtlMinutes) {
        this.bookingRepository = bookingRepository;
        this.eventPublisher = eventPublisher;
        this.flightSearchClient = flightSearchClient;
        this.userServiceClient = userServiceClient;
        this.seatLockService = seatLockService;
        this.holdTtlMinutes = holdTtlMinutes;
        this.heldCounter = Counter.builder("booking.held")
                .description("Number of bookings held")
                .register(meterRegistry);
        this.cancelledCounter = Counter.builder("booking.cancelled")
                .description("Number of bookings cancelled")
                .register(meterRegistry);
        this.expiredCounter = Counter.builder("booking.expired")
                .description("Number of bookings expired")
                .register(meterRegistry);
    }

    @Transactional
    public HoldSeatResponse holdSeat(HoldSeatRequest req, Long userId) {
        // Generate booking code
        String bookingCode = "BK" + LocalDate.now().format(DATE_FORMATTER)
                + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        // Call flight-search-client to check seat availability
        Map<String, Object> seatInfo = flightSearchClient.checkSeat(req.flightId(), req.seatNo());

        if (seatInfo == null || !"AVAILABLE".equals(seatInfo.get("status"))) {
            throw new SeatNotAvailableException(req.flightId(), req.seatNo());
        }

        BigDecimal price = new BigDecimal(seatInfo.get("price").toString());

        // Redis distributed lock
        boolean acquired = seatLockService.acquireLock(
                req.flightId(), req.seatNo(), userId, Duration.ofMinutes(holdTtlMinutes));

        if (!acquired) {
            throw new SeatAlreadyHeldException(req.flightId(), req.seatNo());
        }

        // Build using aggregate factory and save booking
        BookingAggregate booking = BookingAggregate.createHold(bookingCode, userId, req.flightId(), price, holdTtlMinutes);

        BookingItem item = BookingItem.builder()
                .seatNo(req.seatNo())
                .passengerName(req.passengerName())
                .passengerPassport(req.passengerPassport())
                .price(price)
                .build();

        booking.addItem(item);
        booking = bookingRepository.save(booking);

        // Publish event via outbox
        eventPublisher.publishHeld(booking);

        // Increment metric
        heldCounter.increment();

        log.info("Seat held successfully: bookingCode={}, flightId={}, seatNo={}",
                bookingCode, req.flightId(), req.seatNo());

        return HoldSeatResponse.of(
                booking.getId(),
                booking.getBookingCode(),
                booking.getFlightId(),
                req.seatNo(),
                price,
                booking.getCurrency(),
                booking.getExpiresAt()
        );
    }

    @Transactional(readOnly = true)
    public BookingDetailResponse getBookingById(Long id) {
        BookingAggregate booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
        return BookingDetailResponse.of(booking);
    }

    @Transactional(readOnly = true)
    public Page<BookingDetailResponse> getMyBookings(Long userId, String status, int page, int size) {
        Page<BookingAggregate> bookings = bookingRepository.findByUserId(
                userId, PageRequest.of(page, size));
        return bookings.map(BookingDetailResponse::of);
    }

    @Transactional
    public BookingDetailResponse cancelBooking(Long id, Long userId) {
        BookingAggregate booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        if (!booking.getUserId().equals(userId)) {
            throw new UnauthorizedBookingAccessException(userId, id);
        }

        // Delegate state transition to domain aggregate
        booking.cancel();

        // Release Redis seat locks
        final Long flightId = booking.getFlightId();
        booking.getSeatNumbers().forEach(seatNo ->
                seatLockService.releaseLock(flightId, seatNo)
        );

        booking = bookingRepository.save(booking);

        // Enrich and publish event via outbox
        String email = userServiceClient.getUserEmail(booking.getUserId());
        String passengerName = (booking.getItems() == null || booking.getItems().isEmpty())
                ? "Passenger" : booking.getItems().get(0).getPassengerName();
        eventPublisher.publishCancelled(booking, email, passengerName, "Cancelled by user");

        // Increment metric
        cancelledCounter.increment();

        log.info("Booking cancelled: bookingCode={}", booking.getBookingCode());

        return BookingDetailResponse.of(booking);
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void releaseExpiredHolds() {
        List<BookingAggregate> expiredBookings = bookingRepository.findExpiredHolds(
                BookingStatus.HELD, LocalDateTime.now());

        if (expiredBookings.isEmpty()) {
            return;
        }

        for (BookingAggregate booking : expiredBookings) {
            // Delegate state transition to domain aggregate
            booking.expire();

            // Release Redis seat locks
            final Long flightId = booking.getFlightId();
            booking.getSeatNumbers().forEach(seatNo ->
                    seatLockService.releaseLock(flightId, seatNo)
            );

            bookingRepository.save(booking);
            eventPublisher.publishExpired(booking);
        }

        expiredCounter.increment(expiredBookings.size());
        log.info("Released {} expired booking holds", expiredBookings.size());
    }

    @Transactional
    public void confirmBooking(Long bookingId, String paymentId) {
        BookingAggregate booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        // Delegate state transition to domain aggregate
        booking.confirm(paymentId);

        // Release Redis seat locks
        final Long flightId = booking.getFlightId();
        booking.getSeatNumbers().forEach(seatNo ->
                seatLockService.releaseLock(flightId, seatNo)
        );

        booking = bookingRepository.save(booking);

        // Enrich email and flight info
        String email = userServiceClient.getUserEmail(booking.getUserId());
        String passengerName = (booking.getItems() == null || booking.getItems().isEmpty())
                ? "Passenger" : booking.getItems().get(0).getPassengerName();

        Map<String, Object> flightInfo = flightSearchClient.getFlightDetails(booking.getFlightId());
        String flightNo = flightInfo != null ? String.valueOf(flightInfo.get("flightNo")) : "Unknown";
        String from = flightInfo != null ? String.valueOf(flightInfo.get("fromAirport")) : "Unknown";
        String to = flightInfo != null ? String.valueOf(flightInfo.get("toAirport")) : "Unknown";
        String departureTime = flightInfo != null ? String.valueOf(flightInfo.get("departureTime")) : "Unknown";

        // Publish event via outbox
        eventPublisher.publishConfirmed(booking, email, passengerName, flightNo, from, to, departureTime);

        log.info("Booking confirmed and event published: bookingCode={}, paymentId={}",
                booking.getBookingCode(), paymentId);
    }

    @Transactional
    public void handlePaymentFailed(Long bookingId, String reason) {
        BookingAggregate booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        // Delegate state transition to domain aggregate
        booking.cancel();

        // Release Redis seat locks
        final Long flightId = booking.getFlightId();
        booking.getSeatNumbers().forEach(seatNo ->
                seatLockService.releaseLock(flightId, seatNo)
        );

        booking = bookingRepository.save(booking);

        // Enrich details for event
        String email = userServiceClient.getUserEmail(booking.getUserId());
        String passengerName = (booking.getItems() == null || booking.getItems().isEmpty())
                ? "Passenger" : booking.getItems().get(0).getPassengerName();

        // Publish event via outbox
        eventPublisher.publishCancelled(booking, email, passengerName, reason);

        log.info("Booking cancelled due to payment failure: bookingCode={}, reason={}",
                booking.getBookingCode(), reason);
    }
}
