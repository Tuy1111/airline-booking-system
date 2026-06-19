package com.abs.payment.domain.aggregate;

import com.abs.payment.domain.vo.Money;
import com.abs.payment.domain.enums.PaymentGateway;
import com.abs.payment.domain.enums.PaymentMethod;
import com.abs.payment.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private Long id;
    private String paymentCode;
    private Long bookingId;
    private Long userId;
    private Money total;
    private PaymentMethod method;
    private PaymentGateway gateway;
    private PaymentStatus status;
    private String idempotencyKey;
    private String transferCode;
    private String referenceCode;
    private LocalDateTime expiresAt;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    @Builder.Default
    private List<Transaction> transactions = new ArrayList<>();

    public static Payment open(Long bookingId, Long userId, Money total, PaymentMethod method, PaymentGateway gateway) {
        if (bookingId == null) throw new IllegalArgumentException("bookingId required");
        return Payment.builder()
                .bookingId(bookingId).userId(userId).total(total)
                .method(method).gateway(gateway).status(PaymentStatus.PENDING)
                .build();
    }

    public void markSuccessful(String referenceCode, LocalDateTime completedAt) {
        status = PaymentStatus.SUCCESS;
        this.referenceCode = referenceCode;
        this.completedAt = completedAt;
        failureReason = null;
    }

    public void markFailed(String reason, String referenceCode, LocalDateTime completedAt) {
        status = PaymentStatus.FAILED;
        failureReason = reason;
        this.referenceCode = referenceCode;
        this.completedAt = completedAt;
    }
}
