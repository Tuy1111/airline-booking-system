package com.abs.payment.infrastructure.persistence.mapper;

import com.abs.payment.domain.aggregate.RefundAggregate;
import com.abs.payment.infrastructure.persistence.entity.PaymentEntity;
import com.abs.payment.infrastructure.persistence.entity.RefundEntity;

public final class RefundPersistenceMapper {
    private RefundPersistenceMapper() {
    }

    public static RefundAggregate toAggregate(RefundEntity entity) {
        return RefundAggregate.builder()
                .id(entity.getId())
                .paymentId(entity.getPayment().getId())
                .amount(entity.getAmount())
                .reason(entity.getReason())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static RefundEntity toEntity(RefundAggregate aggregate, PaymentEntity payment) {
        return RefundEntity.builder()
                .id(aggregate.getId())
                .payment(payment)
                .amount(aggregate.getAmount())
                .reason(aggregate.getReason())
                .status(aggregate.getStatus())
                .createdAt(aggregate.getCreatedAt())
                .build();
    }
}
