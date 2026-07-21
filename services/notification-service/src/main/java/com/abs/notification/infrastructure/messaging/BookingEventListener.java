package com.abs.notification.infrastructure.messaging;

import com.abs.notification.application.port.in.SendEmailUseCase;
import com.abs.notification.application.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingEventListener {

    private final SendEmailUseCase notificationService;

    @KafkaListener(topics = "${app.notification.kafka.booking-confirmed-topic}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onBookingConfirmed(Map<String, Object> payload) {
        BookingConfirmedEvent evt = confirmedEvent(payload);
        log.info("Rx booking.confirmed: {}", evt.bookingCode());
        notificationService.sendEmail(new SendEmailCommand(
                "BOOKING_CONFIRMED", "vi", evt.userId(), evt.recipientEmail(),
                vars(
                        "bookingCode", evt.bookingCode(),
                        "passengerName", evt.passengerName(),
                        "flightNo", evt.flightNo(),
                        "from", evt.from(),
                        "to", evt.to(),
                        "departureTime", evt.departureTime(),
                        "seatNo", evt.seatNo(),
                        "amount", evt.amount(),
                        "currency", evt.currency()
                ),
                "BOOKING_CONFIRMED:" + evt.bookingCode()));
    }

    @KafkaListener(topics = "${app.notification.kafka.booking-cancelled-topic}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onBookingCancelled(Map<String, Object> payload) {
        BookingCancelledEvent evt = cancelledEvent(payload);
        log.info("Rx booking.cancelled: {}", evt.bookingCode());
        notificationService.sendEmail(new SendEmailCommand(
                "BOOKING_CANCELLED", "vi", evt.userId(), evt.recipientEmail(),
                vars(
                        "bookingCode", evt.bookingCode(),
                        "passengerName", evt.passengerName(),
                        "reason", evt.reason()
                ),
                "BOOKING_CANCELLED:" + evt.bookingCode()));
    }

    // Chú ý: KHÔNG subscribe "payment.failed" ở đây.
    // payment.failed là event hướng tới booking-service (nhả ghế / Saga compensation)
    // và chỉ mang bookingId/userId — thiếu passengerName + recipientEmail nên không
    // render/gửi email được. Khi booking-service xử lý xong, nó publish "booking.cancelled"
    // (đã enrich đủ thông tin khách) → email báo huỷ/thất bại đi qua onBookingCancelled.

    private static Map<String, Object> vars(Object... kv) {
        Map<String, Object> m = new HashMap<>();
        for (int i = 0; i + 1 < kv.length; i += 2) m.put(String.valueOf(kv[i]), kv[i + 1]);
        return m;
    }

    static BookingConfirmedEvent confirmedEvent(Map<String, Object> payload) {
        return new BookingConfirmedEvent(
                text(payload, "bookingCode"),
                number(payload, "userId").longValue(),
                text(payload, "passengerName"),
                text(payload, "recipientEmail"),
                text(payload, "flightNo"),
                text(payload, "from"),
                text(payload, "to"),
                text(payload, "departureTime"),
                text(payload, "seatNo"),
                decimal(payload, "amount"),
                text(payload, "currency"));
    }

    static BookingCancelledEvent cancelledEvent(Map<String, Object> payload) {
        return new BookingCancelledEvent(
                text(payload, "bookingCode"),
                number(payload, "userId").longValue(),
                text(payload, "passengerName"),
                text(payload, "recipientEmail"),
                text(payload, "reason"));
    }

    private static String text(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing Kafka payload field: " + key);
        }
        return String.valueOf(value);
    }

    private static Number number(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value instanceof Number number) {
            return number;
        }
        return Long.parseLong(text(payload, key));
    }

    private static BigDecimal decimal(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value instanceof BigDecimal decimal) {
            return decimal;
        }
        if (value instanceof Number number) {
            return new BigDecimal(number.toString());
        }
        return new BigDecimal(text(payload, key));
    }
}
