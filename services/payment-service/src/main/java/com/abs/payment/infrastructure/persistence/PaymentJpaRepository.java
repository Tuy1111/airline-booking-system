package com.abs.payment.infrastructure.persistence;

import com.abs.payment.domain.enums.PaymentStatus;
import com.abs.payment.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByPaymentCode(String paymentCode);
    Optional<PaymentEntity> findByIdempotencyKey(String idempotencyKey);
    Optional<PaymentEntity> findByTransferCode(String transferCode);
    Optional<PaymentEntity> findByReferenceCode(String referenceCode);
    List<PaymentEntity> findByBookingId(Long bookingId);
    List<PaymentEntity> findByStatusAndExpiresAtBefore(PaymentStatus status, LocalDateTime time);

    /**
     * Chuyển PENDING → FAILED một cách atomic, CHỈ khi row vẫn còn PENDING.
     * Trả về số row thật sự đổi (1 = đã đổi, 0 = đã bị webhook/instance khác xử lý).
     * Nhờ điều kiện {@code status = PENDING} ngay trong WHERE, một webhook
     * đánh SUCCESS song song (hoặc một instance scan khác) sẽ không bị ghi đè.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE PaymentEntity p
               SET p.status = com.abs.payment.domain.enums.PaymentStatus.FAILED,
                   p.failureReason = :reason,
                   p.completedAt = :now
             WHERE p.id = :id
               AND p.status = com.abs.payment.domain.enums.PaymentStatus.PENDING
            """)
    int markExpiredIfPending(@Param("id") Long id,
                             @Param("reason") String reason,
                             @Param("now") LocalDateTime now);
}
