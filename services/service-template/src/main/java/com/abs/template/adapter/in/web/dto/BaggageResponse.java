package com.abs.template.adapter.in.web.dto;

import com.abs.template.domain.aggregate.BaggageAggregate;

import java.math.BigDecimal;

/** Response JSON — chiếu aggregate domain ra ngoài, giấu cấu trúc nội bộ. */
public record BaggageResponse(
        String id,
        String bookingRef,
        BigDecimal weightKg,
        String status
) {
    public static BaggageResponse from(BaggageAggregate b) {
        return new BaggageResponse(
                b.id().toString(),
                b.bookingRef(),
                b.weight().kilograms(),
                b.status().name());
    }
}
