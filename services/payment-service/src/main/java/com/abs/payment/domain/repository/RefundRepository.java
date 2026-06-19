package com.abs.payment.domain.repository;

import com.abs.payment.domain.aggregate.Refund;

public interface RefundRepository {
    Refund save(Refund aggregate);
}
