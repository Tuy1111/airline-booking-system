package com.abs.payment.infrastructure.persistence.mapper;

import com.abs.payment.domain.aggregate.Payment;
import com.abs.payment.domain.vo.Money;
import com.abs.payment.infrastructure.persistence.entity.PaymentEntity;

public final class PaymentPersistenceMapper {
    private PaymentPersistenceMapper() {
    }

    public static Payment toAggregate(PaymentEntity entity) {
        return Payment.builder()
                .id(entity.getId())
                .paymentCode(entity.getPaymentCode())
                .bookingId(entity.getBookingId())
                .userId(entity.getUserId())
                .total(Money.of(entity.getAmount(), entity.getCurrency()))
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

    public static PaymentEntity toEntity(Payment aggregate) {
        return PaymentEntity.builder()
                .id(aggregate.getId())
                .paymentCode(aggregate.getPaymentCode())
                .bookingId(aggregate.getBookingId())
                .userId(aggregate.getUserId())
                .amount(aggregate.getTotal().amount())
                .currency(aggregate.getTotal().currency())
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
