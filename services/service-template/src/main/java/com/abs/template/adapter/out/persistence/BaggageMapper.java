package com.abs.template.adapter.out.persistence;

import com.abs.template.domain.aggregate.BaggageAggregate;
import com.abs.template.domain.vo.BaggageId;
import com.abs.template.domain.vo.Weight;

/** Ánh xạ hai chiều giữa aggregate domain và JPA entity. Thuần tĩnh, không trạng thái. */
final class BaggageMapper {

    private BaggageMapper() {}

    static BaggageJpaEntity toJpa(BaggageAggregate domain) {
        return BaggageJpaEntity.builder()
                .id(domain.id().value().toString())
                .bookingRef(domain.bookingRef())
                .weightKg(domain.weight().kilograms())
                .status(domain.status())
                .build();
    }

    static BaggageAggregate toDomain(BaggageJpaEntity jpa) {
        return BaggageAggregate.rehydrate(
                BaggageId.of(jpa.getId()),
                jpa.getBookingRef(),
                Weight.ofKg(jpa.getWeightKg()),
                jpa.getStatus());
    }
}
