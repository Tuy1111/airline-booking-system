package com.abs.payment.domain.repository;

import com.abs.payment.domain.aggregate.TransactionAggregate;

public interface TransactionRepository {
    TransactionAggregate save(TransactionAggregate aggregate);
}
