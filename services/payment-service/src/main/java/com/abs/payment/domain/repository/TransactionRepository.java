package com.abs.payment.domain.repository;

import com.abs.payment.domain.aggregate.Transaction;

public interface TransactionRepository {
    Transaction save(Transaction aggregate);
}
