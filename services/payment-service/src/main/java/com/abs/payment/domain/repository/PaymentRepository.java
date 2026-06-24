package com.abs.payment.domain.repository;

import com.abs.payment.domain.aggregate.PaymentAggregate;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    PaymentAggregate save(PaymentAggregate aggregate);
    Optional<PaymentAggregate> findById(Long id);
    Optional<PaymentAggregate> findByPaymentCode(String paymentCode);
    Optional<PaymentAggregate> findByIdempotencyKey(String idempotencyKey);
    Optional<PaymentAggregate> findByTransferCode(String transferCode);
    Optional<PaymentAggregate> findByReferenceCode(String referenceCode);
    List<PaymentAggregate> findByBookingId(Long bookingId);
}
