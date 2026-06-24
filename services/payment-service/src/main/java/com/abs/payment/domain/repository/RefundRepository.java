package com.abs.payment.domain.repository;

import com.abs.payment.domain.aggregate.RefundAggregate;

public interface RefundRepository {
    RefundAggregate save(RefundAggregate aggregate);
}
