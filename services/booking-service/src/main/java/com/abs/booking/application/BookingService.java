package com.abs.booking.application;

import com.abs.booking.application.dto.BookingDetailResponse;
import com.abs.booking.application.dto.HoldSeatRequest;
import com.abs.booking.application.dto.HoldSeatResponse;
import com.abs.booking.domain.aggregate.BookingAggregate;
import com.abs.booking.domain.aggregate.BookingItem;
import com.abs.booking.domain.vo.BookingStatus;
import com.abs.booking.domain.repository.BookingRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
    private final RestTemplate restTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final Counter heldCounter;
    private final Counter cancelledCounter;
    private final Counter expiredCounter;
    private final int holdTtlMinutes;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public BookingService(BookingRepository bookingRepository,
                          BookingEventPublisher eventPublisher,
                          RestTemplate restTemplate,
                          StringRedisTemplate stringRedisTemplate,
                          MeterRegistry meterRegistry,
                          @Value("${booking.hold.ttl-minutes:10}") int holdTtlMinutes) {
        this.bookingRepository = bookingRepository;
        this.eventPublisher = eventPublisher;
        this.restTemplate = restTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
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

        // Call flight-search-service to check seat availability
        String seatUrl = "http://localhost:8081/api/v1/flights/" + req.flightId()
                + "/seats/" + req.seatNo();

        @SuppressWarnings("unchecked")
        Map<String, Object> seatInfo = restTemplate.getForObject(seatUrl, Map.class);

        if (seatInfo == null || !"AVAILABLE".equals(seatInfo.get("status"))) {
            throw new RuntimeException("Seat not available");
        }

        BigDecimal price = new BigDecimal(seatInfo.get("price").toString());

        // Redis SETNX to lock the seat
        String redisKey = "seat:" + req.flightId() + ":" + req.seatNo();
        Boolean acquired = stringRedisTemplate.opsForValue()
                .setIfAbsent(redisKey, userId.toString(), Duration.ofMinutes(holdTtlMinutes));

        if (Boolean.FALSE.equals(acquired)) {
            throw new RuntimeException("Seat is already held by another user");
        }

        // Build and save booking
        LocalDateTime now = LocalDateTime.now();
        BookingAggregate booking = BookingAggregate.builder()
                .bookingCode(bookingCode)
                .userId(userId)
                .flightId(req.flightId())
                .status(BookingStatus.HELD)
                .totalAmount(price)
                .currency("VND")
                .heldAt(now)
                .expiresAt(now.plusMinutes(holdTtlMinutes))
                .build();

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
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
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
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        if (!booking.getUserId().equals(userId)) {
            throw new RuntimeException("You are not authorized to cancel this booking");
        }

        if (booking.getStatus() != BookingStatus.HELD && booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Booking cannot be cancelled in status: " + booking.getStatus());
        }

        // Release Redis seat locks
        if (booking.getItems() != null) {
            for (BookingItem item : booking.getItems()) {
                String redisKey = "seat:" + booking.getFlightId() + ":" + item.getSeatNo();
                stringRedisTemplate.delete(redisKey);
                log.debug("Released Redis lock for key: {}", redisKey);
            }
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking = bookingRepository.save(booking);

        // Enrich and publish event via outbox
        String email = getUserEmail(booking.getUserId());
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
            booking.setStatus(BookingStatus.EXPIRED);

            // Release Redis seat locks
            if (booking.getItems() != null) {
                for (BookingItem item : booking.getItems()) {
                    String redisKey = "seat:" + booking.getFlightId() + ":" + item.getSeatNo();
                    stringRedisTemplate.delete(redisKey);
                }
            }

            bookingRepository.save(booking);
            eventPublisher.publishExpired(booking);
        }

        expiredCounter.increment(expiredBookings.size());
        log.info("Released {} expired booking holds", expiredBookings.size());
    }

    @Transactional
    public void confirmBooking(Long bookingId, String paymentId) {
        BookingAggregate booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        if (booking.getStatus() != BookingStatus.HELD) {
            log.warn("Booking {} is not in HELD state, status is {}. Cannot confirm.", bookingId, booking.getStatus());
            return;
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentId(paymentId);
        booking.setConfirmedAt(LocalDateTime.now());
        booking = bookingRepository.save(booking);

        // Release Redis seat locks
        if (booking.getItems() != null) {
            for (BookingItem item : booking.getItems()) {
                String redisKey = "seat:" + booking.getFlightId() + ":" + item.getSeatNo();
                stringRedisTemplate.delete(redisKey);
                log.debug("Released Redis lock for key: {}", redisKey);
            }
        }

        // Enrich email and flight info
        String email = getUserEmail(booking.getUserId());
        String passengerName = (booking.getItems() == null || booking.getItems().isEmpty())
                ? "Passenger" : booking.getItems().get(0).getPassengerName();

        Map<String, Object> flightInfo = getFlightDetails(booking.getFlightId());
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
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        if (booking.getStatus() != BookingStatus.HELD && booking.getStatus() != BookingStatus.CONFIRMED) {
            log.warn("Booking {} status is {}. Cannot cancel on payment failure.", bookingId, booking.getStatus());
            return;
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking = bookingRepository.save(booking);

        // Release Redis seat locks
        if (booking.getItems() != null) {
            for (BookingItem item : booking.getItems()) {
                String redisKey = "seat:" + booking.getFlightId() + ":" + item.getSeatNo();
                stringRedisTemplate.delete(redisKey);
                log.debug("Released Redis lock for key: {}", redisKey);
            }
        }

        // Enrich details for event
        String email = getUserEmail(booking.getUserId());
        String passengerName = (booking.getItems() == null || booking.getItems().isEmpty())
                ? "Passenger" : booking.getItems().get(0).getPassengerName();

        // Publish event via outbox
        eventPublisher.publishCancelled(booking, email, passengerName, reason);

        log.info("Booking cancelled due to payment failure: bookingCode={}, reason={}",
                booking.getBookingCode(), reason);
    }

    private String getUserEmail(Long userId) {
        try {
            String userUrl = "http://localhost:8083/api/v1/users/" + userId;
            @SuppressWarnings("unchecked")
            Map<String, Object> userInfo = restTemplate.getForObject(userUrl, Map.class);
            if (userInfo != null && userInfo.containsKey("email")) {
                return String.valueOf(userInfo.get("email"));
            }
        } catch (Exception e) {
            log.warn("Failed to fetch email for userId={} from user-service: {}", userId, e.getMessage());
        }
        return "user" + userId + "@example.com";
    }

    private Map<String, Object> getFlightDetails(Long flightId) {
        try {
            String flightUrl = "http://localhost:8081/api/v1/flights/" + flightId;
            @SuppressWarnings("unchecked")
            Map<String, Object> flightInfo = restTemplate.getForObject(flightUrl, Map.class);
            return flightInfo;
        } catch (Exception e) {
            log.error("Failed to fetch flight details for flightId={}: {}", flightId, e.getMessage());
            return null;
        }
    }
}
