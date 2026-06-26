package com.abs.template.domain.event;

import java.time.Instant;

/**
 * Marker cho mọi Domain Event — "một sự việc có ý nghĩa nghiệp vụ đã xảy ra".
 *
 * <p>Event là bất biến, đặt tên ở thì quá khứ (BaggageCheckedIn, không phải CheckInBaggage).
 * Aggregate ghi nhận event; adapter ở rìa hệ thống chịu trách nhiệm publish ra ngoài
 * (Kafka/RabbitMQ…) thông qua port {@code DomainEventPublisher}.
 */
public interface DomainEvent {
    Instant occurredAt();
}
