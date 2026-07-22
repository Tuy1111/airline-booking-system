package com.abs.payment.infrastructure.persistence.mapper;

import com.abs.payment.domain.aggregate.Transaction;
import com.abs.payment.infrastructure.persistence.entity.PaymentEntity;
import com.abs.payment.infrastructure.persistence.entity.TransactionEntity;

public final class TransactionPersistenceMapper {
    private TransactionPersistenceMapper() {
    }

    public static Transaction toAggregate(TransactionEntity entity) {
        return Transaction.builder()
                .id(entity.getId())
                .paymentId(entity.getPayment().getId())
                .gatewayTxnId(entity.getGatewayTxnId())
                .gatewayResponse(entity.getGatewayResponse())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static TransactionEntity toEntity(Transaction aggregate, PaymentEntity payment) {
        return TransactionEntity.builder()
                .id(aggregate.getId())
                .payment(payment)
                .gatewayTxnId(aggregate.getGatewayTxnId())
                .gatewayResponse(aggregate.getGatewayResponse())
                .status(aggregate.getStatus())
                .createdAt(aggregate.getCreatedAt())
                .build();
    }
}
