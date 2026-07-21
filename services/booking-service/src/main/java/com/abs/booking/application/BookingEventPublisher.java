package com.abs.booking.application;

import com.abs.booking.domain.aggregate.BookingAggregate;
import com.abs.booking.infrastructure.persistence.outbox.OutboxEvent;
import com.abs.booking.infrastructure.persistence.outbox.OutboxStatus;
import com.abs.booking.infrastructure.persistence.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingEventPublisher {

    private final OutboxEventRepository outboxEventRepository;

    public void publishHeld(BookingAggregate booking) {
        OutboxEvent event = buildEvent(booking, "BookingHeld");
        outboxEventRepository.save(event);
        log.info("Published BookingHeld event for booking: {}", booking.getBookingCode());
    }

    public void publishExpired(BookingAggregate booking) {
        OutboxEvent event = buildEvent(booking, "BookingExpired");
        outboxEventRepository.save(event);
        log.info("Published BookingExpired event for booking: {}", booking.getBookingCode());
    }

    public void publishConfirmed(BookingAggregate booking, String recipientEmail, String passengerName,
                                 String flightNo, String from, String to, String departureTime) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("bookingCode", booking.getBookingCode());
        payload.put("userId", booking.getUserId());
        payload.put("passengerName", passengerName);
        payload.put("recipientEmail", recipientEmail);
        payload.put("flightNo", flightNo);
        payload.put("from", from);
        payload.put("to", to);
        payload.put("departureTime", departureTime);
        payload.put("seatNo", booking.getItems() == null || booking.getItems().isEmpty() ? "" : booking.getItems().get(0).getSeatNo());
        payload.put("extraBaggageKg", booking.getBaggageWeightKg() == null ? 0 : booking.getBaggageWeightKg());
        payload.put("amount", booking.getTotalAmount());
        payload.put("currency", booking.getCurrency());


        OutboxEvent event = OutboxEvent.builder()
                .aggregateType("Booking")
                .aggregateId(booking.getId())
                .eventType("BookingConfirmed")
                .payload(payload)
                .status(OutboxStatus.PENDING)
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .build();
        outboxEventRepository.save(event);
        log.info("Published BookingConfirmed event for booking: {}", booking.getBookingCode());
    }

    public void publishCancelled(BookingAggregate booking, String recipientEmail, String passengerName, String reason) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("bookingCode", booking.getBookingCode());
        payload.put("userId", booking.getUserId());
        payload.put("passengerName", passengerName);
        payload.put("recipientEmail", recipientEmail);
        payload.put("reason", reason);

        OutboxEvent event = OutboxEvent.builder()
                .aggregateType("Booking")
                .aggregateId(booking.getId())
                .eventType("BookingCancelled")
                .payload(payload)
                .status(OutboxStatus.PENDING)
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .build();
        outboxEventRepository.save(event);
        log.info("Published BookingCancelled event for booking: {}", booking.getBookingCode());
    }

    private OutboxEvent buildEvent(BookingAggregate booking, String eventType) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("bookingId", booking.getId());
        payload.put("bookingCode", booking.getBookingCode());
        payload.put("userId", booking.getUserId());
        payload.put("flightId", booking.getFlightId());
        payload.put("status", booking.getStatus().name());
        payload.put("totalAmount", booking.getTotalAmount());
        payload.put("currency", booking.getCurrency());

        return OutboxEvent.builder()
                .aggregateType("Booking")
                .aggregateId(booking.getId())
                .eventType(eventType)
                .payload(payload)
                .status(OutboxStatus.PENDING)
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
