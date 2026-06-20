package com.abs.notification.infrastructure.messaging;

import com.abs.notification.application.port.in.SendEmailUseCase;
import com.abs.notification.application.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingEventListener {

    private final SendEmailUseCase notificationService;

    @KafkaListener(topics = "${app.notification.kafka.booking-confirmed-topic}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onBookingConfirmed(BookingConfirmedEvent evt) {
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
    public void onBookingCancelled(BookingCancelledEvent evt) {
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
}
