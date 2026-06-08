package com.abs.template.domain.event;

import com.abs.template.domain.vo.BaggageId;
import com.abs.template.domain.vo.Weight;

import java.time.Instant;

/** Hành lý đã được ký gửi hợp lệ (trong hạn mức). */
public record BaggageCheckedIn(
        BaggageId baggageId,
        String bookingRef,
        Weight weight,
        Instant occurredAt
) implements DomainEvent {

    public BaggageCheckedIn(BaggageId baggageId, String bookingRef, Weight weight) {
        this(baggageId, bookingRef, weight, Instant.now());
    }
}
