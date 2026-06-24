package com.abs.template.domain.event;

import com.abs.template.domain.vo.BaggageId;
import com.abs.template.domain.vo.Weight;

import java.time.Instant;

/** Hành lý vượt hạn mức — bị giữ lại chờ xử lý. */
public record BaggageOverweightFlagged(
        BaggageId baggageId,
        String bookingRef,
        Weight weight,
        Weight allowance,
        Instant occurredAt
) implements DomainEvent {

    public BaggageOverweightFlagged(BaggageId baggageId, String bookingRef, Weight weight, Weight allowance) {
        this(baggageId, bookingRef, weight, allowance, Instant.now());
    }
}
