package com.abs.payment.infrastructure.persistence;

import com.abs.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentCode(String paymentCode);
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
    List<Payment> findByBookingId(Long bookingId);
}
