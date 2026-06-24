package com.abs.template.domain.port.out;

import com.abs.template.domain.event.DomainEvent;

import java.util.List;

/**
 * Outbound Port (driven) — phát Domain Event ra hạ tầng messaging.
 *
 * <p>Domain chỉ biết "tôi cần công bố những sự kiện này"; còn việc đẩy qua
 * Kafka/RabbitMQ hay outbox là chuyện của adapter. Nhờ vậy lõi test được mà
 * không cần broker thật.
 */
public interface DomainEventPublisher {
    void publishAll(List<DomainEvent> events);
}
