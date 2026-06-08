package com.abs.payment.infrastructure.persistence;

import com.abs.payment.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByPaymentCode(String paymentCode);
    Optional<PaymentEntity> findByIdempotencyKey(String idempotencyKey);
    Optional<PaymentEntity> findByTransferCode(String transferCode);
    Optional<PaymentEntity> findByReferenceCode(String referenceCode);
    List<PaymentEntity> findByBookingId(Long bookingId);
}
