package com.abs.payment.domain.repository;

import com.abs.payment.domain.aggregate.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment aggregate);
    Optional<Payment> findById(Long id);
    Optional<Payment> findByPaymentCode(String paymentCode);
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
    Optional<Payment> findByTransferCode(String transferCode);
    Optional<Payment> findByReferenceCode(String referenceCode);
    List<Payment> findByBookingId(Long bookingId);
}
