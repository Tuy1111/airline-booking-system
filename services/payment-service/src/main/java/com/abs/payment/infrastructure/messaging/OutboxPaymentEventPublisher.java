package com.abs.payment.infrastructure.messaging;

import com.abs.payment.application.PaymentEventPublisher;
import com.abs.payment.application.dto.PaymentCompletedEvent;
import com.abs.payment.application.dto.PaymentFailedEvent;
import com.abs.payment.infrastructure.persistence.outbox.OutboxEvent;
import com.abs.payment.infrastructure.persistence.outbox.OutboxStatus;
import com.abs.payment.infrastructure.persistence.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Ghi event vào outbox table TRONG CÙNG transaction với payment update —
 * đảm bảo atomic "DB commit ↔ event sẽ được publish". OutboxRelay sẽ đẩy
 * lên Kafka sau (at-least-once).
 */
@Component
@RequiredArgsConstructor
public class OutboxPaymentEventPublisher implements PaymentEventPublisher {

    private final OutboxEventRepository outboxRepo;
    private final ObjectMapper objectMapper;

    @Override
    public void publishCompleted(PaymentCompletedEvent evt) {
        outboxRepo.save(OutboxEvent.builder()
                .aggregateType("Payment")
                .aggregateId(evt.paymentId())
                .eventType("payment.completed")
                .payload(toMap(evt))
                .status(OutboxStatus.PENDING)
                .retryCount(0)
                .build());
    }

    @Override
    public void publishFailed(PaymentFailedEvent evt) {
        outboxRepo.save(OutboxEvent.builder()
                .aggregateType("Payment")
                .aggregateId(evt.paymentId())
                .eventType("payment.failed")
                .payload(toMap(evt))
                .status(OutboxStatus.PENDING)
                .retryCount(0)
                .build());
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private Map<String, Object> toMap(Object o) {
        return objectMapper.convertValue(o, Map.class);
    }
}
