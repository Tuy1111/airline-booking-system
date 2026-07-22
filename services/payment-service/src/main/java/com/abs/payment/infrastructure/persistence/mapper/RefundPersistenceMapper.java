package com.abs.payment.infrastructure.persistence.mapper;

import com.abs.payment.domain.aggregate.Refund;
import com.abs.payment.domain.vo.Money;
import com.abs.payment.infrastructure.persistence.entity.PaymentEntity;
import com.abs.payment.infrastructure.persistence.entity.RefundEntity;

public final class RefundPersistenceMapper {
    private RefundPersistenceMapper() {
    }

    public static Refund toAggregate(RefundEntity entity) {
        return Refund.builder()
                .id(entity.getId())
                .paymentId(entity.getPayment().getId())
                .amount(Money.vnd(entity.getAmount()))
                .reason(entity.getReason())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static RefundEntity toEntity(Refund aggregate, PaymentEntity payment) {
        return RefundEntity.builder()
                .id(aggregate.getId())
                .payment(payment)
                .amount(aggregate.getAmount().amount())
                .reason(aggregate.getReason())
                .status(aggregate.getStatus())
                .createdAt(aggregate.getCreatedAt())
                .build();
    }
}
