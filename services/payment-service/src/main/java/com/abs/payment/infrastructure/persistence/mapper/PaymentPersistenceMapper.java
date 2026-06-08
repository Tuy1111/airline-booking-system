package com.abs.payment.infrastructure.persistence.mapper;

import com.abs.payment.domain.aggregate.PaymentAggregate;
import com.abs.payment.infrastructure.persistence.entity.PaymentEntity;

public final class PaymentPersistenceMapper {
    private PaymentPersistenceMapper() {
    }

    public static PaymentAggregate toAggregate(PaymentEntity entity) {
        return PaymentAggregate.builder()
                .id(entity.getId())
                .paymentCode(entity.getPaymentCode())
                .bookingId(entity.getBookingId())
                .userId(entity.getUserId())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .method(entity.getMethod())
                .gateway(entity.getGateway())
                .status(entity.getStatus())
                .idempotencyKey(entity.getIdempotencyKey())
                .transferCode(entity.getTransferCode())
                .referenceCode(entity.getReferenceCode())
                .expiresAt(entity.getExpiresAt())
                .failureReason(entity.getFailureReason())
                .createdAt(entity.getCreatedAt())
                .completedAt(entity.getCompletedAt())
                .build();
    }

    public static PaymentEntity toEntity(PaymentAggregate aggregate) {
        return PaymentEntity.builder()
                .id(aggregate.getId())
                .paymentCode(aggregate.getPaymentCode())
                .bookingId(aggregate.getBookingId())
                .userId(aggregate.getUserId())
                .amount(aggregate.getAmount())
                .currency(aggregate.getCurrency())
                .method(aggregate.getMethod())
                .gateway(aggregate.getGateway())
                .status(aggregate.getStatus())
                .idempotencyKey(aggregate.getIdempotencyKey())
                .transferCode(aggregate.getTransferCode())
                .referenceCode(aggregate.getReferenceCode())
                .expiresAt(aggregate.getExpiresAt())
                .failureReason(aggregate.getFailureReason())
                .createdAt(aggregate.getCreatedAt())
                .completedAt(aggregate.getCompletedAt())
                .build();
    }
}
