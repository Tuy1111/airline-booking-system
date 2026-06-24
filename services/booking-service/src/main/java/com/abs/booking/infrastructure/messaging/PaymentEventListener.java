package com.abs.booking.infrastructure.messaging;

import com.abs.booking.application.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final BookingService bookingService;

    @KafkaListener(topics = "${app.kafka.topics.payment-completed}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onPaymentCompleted(Map<String, Object> payload) {
        log.info("Received payment.completed event: {}", payload);
        try {
            Long bookingId = Long.valueOf(payload.get("bookingId").toString());
            String paymentCode = String.valueOf(payload.get("paymentCode"));
            bookingService.confirmBooking(bookingId, paymentCode);
        } catch (Exception e) {
            log.error("Failed to process payment.completed event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${app.kafka.topics.payment-failed}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onPaymentFailed(Map<String, Object> payload) {
        log.info("Received payment.failed event: {}", payload);
        try {
            Long bookingId = Long.valueOf(payload.get("bookingId").toString());
            String reason = payload.containsKey("reason") ? String.valueOf(payload.get("reason")) : "Payment failed";
            bookingService.handlePaymentFailed(bookingId, reason);
        } catch (Exception e) {
            log.error("Failed to process payment.failed event: {}", e.getMessage(), e);
        }
    }
}
