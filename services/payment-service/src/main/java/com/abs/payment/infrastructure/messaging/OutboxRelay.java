package com.abs.payment.infrastructure.messaging;

import com.abs.payment.infrastructure.persistence.outbox.OutboxEvent;
import com.abs.payment.infrastructure.persistence.outbox.OutboxStatus;
import com.abs.payment.infrastructure.persistence.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Transactional Outbox relay.
 * Mỗi 2s: pick PENDING events (FOR UPDATE SKIP LOCKED) → send Kafka → mark SENT.
 * Topic được map từ eventType: "payment.completed" → app.kafka.topics.payment-completed, etc.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelay {

    private final OutboxEventRepository outboxRepo;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.payment-completed}") private String topicCompleted;
    @Value("${app.kafka.topics.payment-failed}")    private String topicFailed;
    @Value("${app.outbox.batch-size:50}")           private int batchSize;

    @Scheduled(fixedDelayString = "${app.outbox.poll-interval-ms:2000}")
    @Transactional
    public void relay() {
        List<OutboxEvent> batch = outboxRepo.lockPending(OutboxStatus.PENDING.name(), batchSize);
        if (batch.isEmpty()) return;

        for (OutboxEvent evt : batch) {
            String topic = topicFor(evt.getEventType());
            if (topic == null) {
                evt.setStatus(OutboxStatus.FAILED);
                evt.setRetryCount((evt.getRetryCount() == null ? 0 : evt.getRetryCount()) + 1);
                log.warn("Outbox event {}: unknown eventType={}", evt.getId(), evt.getEventType());
                continue;
            }
            try {
                String key = String.valueOf(evt.getAggregateId());
                Map<String, Object> payload = evt.getPayload();
                CompletableFuture<?> fut = kafkaTemplate.send(topic, key, payload);
                fut.get(); // wait for ack inside the relay tx
                evt.setStatus(OutboxStatus.SENT);
                evt.setSentAt(LocalDateTime.now());
                log.debug("Outbox sent → topic={} key={} eventType={}",
                        topic, key, evt.getEventType());
            } catch (Exception ex) {
                evt.setRetryCount((evt.getRetryCount() == null ? 0 : evt.getRetryCount()) + 1);
                if (evt.getRetryCount() >= 10) evt.setStatus(OutboxStatus.FAILED);
                log.error("Outbox send failed (retry={}): {}", evt.getRetryCount(), ex.getMessage());
            }
        }
    }

    private String topicFor(String eventType) {
        return switch (eventType) {
            case "payment.completed" -> topicCompleted;
            case "payment.failed"    -> topicFailed;
            default                  -> null;
        };
    }
}
