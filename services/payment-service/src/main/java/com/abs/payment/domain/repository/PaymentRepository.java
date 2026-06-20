package com.abs.payment.domain.repository;

import com.abs.payment.domain.aggregate.Payment;

import java.time.LocalDateTime;
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

    /** Các payment còn PENDING nhưng đã quá hạn {@code expiresAt < now}. */
    List<Payment> findExpiredPending(LocalDateTime now);

    /**
     * Đổi PENDING → FAILED atomic, chỉ khi row còn PENDING.
     * @return true nếu row thật sự được đổi (1 row), false nếu đã terminal (webhook/instance khác xử lý).
     */
    boolean markExpiredIfPending(Long id, String reason, LocalDateTime now);
}
