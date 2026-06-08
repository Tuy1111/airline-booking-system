package com.abs.template.adapter.out.messaging;

import com.abs.template.domain.event.DomainEvent;
import com.abs.template.domain.port.out.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Outbound Adapter — hiện thực {@link DomainEventPublisher}.
 *
 * <p>Bản template chỉ ghi log để chạy được ngay không cần broker. Khi lên thật, thay
 * bằng adapter Kafka/RabbitMQ hoặc pattern Outbox (xem cách payment-service/notification-service
 * dùng outbox + spring-kafka) — lõi domain KHÔNG phải sửa một dòng nào.
 */
@Slf4j
@Component
public class LoggingDomainEventPublisher implements DomainEventPublisher {

    @Override
    public void publishAll(List<DomainEvent> events) {
        for (DomainEvent event : events) {
            // TODO(prod): publish ra Kafka/outbox thay vì log.
            log.info("[domain-event] {} @ {} → {}",
                    event.getClass().getSimpleName(), event.occurredAt(), event);
        }
    }
}
