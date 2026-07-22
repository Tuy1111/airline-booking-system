package com.abs.booking.infrastructure.messaging;

import com.abs.booking.application.usecase.ConfirmBookingUseCase;
import com.abs.booking.application.usecase.HandlePaymentFailedUseCase;
import com.abs.booking.domain.exception.BookingNotFoundException;
import com.abs.booking.domain.exception.InvalidBookingStateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final ConfirmBookingUseCase confirmBookingUseCase;
    private final HandlePaymentFailedUseCase handlePaymentFailedUseCase;

    @KafkaListener(topics = "${app.kafka.topics.payment-completed}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onPaymentCompleted(Map<String, Object> payload) {
        log.info("Received payment.completed event: {}", payload);
        try {
            Long bookingId = Long.valueOf(payload.get("bookingId").toString());
            String paymentCode = String.valueOf(payload.get("paymentCode"));
            confirmBookingUseCase.execute(bookingId, paymentCode);
        } catch (InvalidBookingStateException e) {
            log.warn("Cannot confirm booking {} due to invalid state: {}", payload.get("bookingId"), e.getMessage());
        } catch (BookingNotFoundException e) {
            log.error("Booking not found for payment.completed: {}", payload.get("bookingId"));
        } catch (Exception e) {
            log.error("Transient error processing payment.completed event, rethrowing for retry: {}", e.getMessage(), e);
            throw e; // Rethrow to trigger Kafka retry
        }
    }

    @KafkaListener(topics = "${app.kafka.topics.payment-failed}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onPaymentFailed(Map<String, Object> payload) {
        log.info("Received payment.failed event: {}", payload);
        try {
            Long bookingId = Long.valueOf(payload.get("bookingId").toString());
            String reason = payload.containsKey("reason") ? String.valueOf(payload.get("reason")) : "Payment failed";
            handlePaymentFailedUseCase.execute(bookingId, reason);
        } catch (InvalidBookingStateException e) {
            log.warn("Cannot cancel booking {} due to invalid state: {}", payload.get("bookingId"), e.getMessage());
        } catch (BookingNotFoundException e) {
            log.error("Booking not found for payment.failed: {}", payload.get("bookingId"));
        } catch (Exception e) {
            log.error("Transient error processing payment.failed event, rethrowing for retry: {}", e.getMessage(), e);
            throw e; // Rethrow to trigger Kafka retry
        }
    }
}
