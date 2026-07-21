package com.abs.notification.infrastructure.messaging;

import com.abs.notification.application.usecase.SendDirectNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/** Creates in-app notifications for every payment terminal state. */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final SendDirectNotificationService notificationService;

    @KafkaListener(topics = "${app.notification.kafka.payment-completed-topic}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onPaymentCompleted(Map<String, Object> payload) {
        Long userId = number(payload, "userId").longValue();
        String paymentCode = text(payload, "paymentCode");
        String amount = decimal(payload, "amount").toPlainString();
        notificationService.send(
                userId,
                "Thanh toán thành công",
                "Thanh toán " + paymentCode + " đã thành công với số tiền " + amount + " VND.",
                "PAYMENT_COMPLETED",
                "PUSH:PAYMENT_COMPLETED:" + paymentCode);
        log.info("In-app payment success notification created: paymentCode={}", paymentCode);
    }

    @KafkaListener(topics = "${app.notification.kafka.payment-failed-topic}",
            containerFactory = "kafkaListenerContainerFactory")
    public void onPaymentFailed(Map<String, Object> payload) {
        Long userId = number(payload, "userId").longValue();
        String paymentCode = text(payload, "paymentCode");
        String reason = text(payload, "reason");
        notificationService.send(
                userId,
                "Thanh toán thất bại",
                "Thanh toán " + paymentCode + " thất bại. Lý do: " + reason,
                "PAYMENT_FAILED",
                "PUSH:PAYMENT_FAILED:" + paymentCode);
        log.info("In-app payment failure notification created: paymentCode={}", paymentCode);
    }

    private static String text(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        return value == null ? "Không xác định" : String.valueOf(value);
    }

    private static Number number(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value instanceof Number number) return number;
        return Long.parseLong(text(payload, key));
    }

    private static BigDecimal decimal(Map<String, Object> payload, String key) {
        Object value = payload.get(key);
        if (value instanceof BigDecimal decimal) return decimal;
        if (value instanceof Number number) return new BigDecimal(number.toString());
        return new BigDecimal(text(payload, key));
    }
}
