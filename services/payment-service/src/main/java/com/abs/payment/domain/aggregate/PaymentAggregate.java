package com.abs.payment.domain.aggregate;

import com.abs.payment.domain.vo.PaymentGateway;
import com.abs.payment.domain.vo.PaymentMethod;
import com.abs.payment.domain.vo.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAggregate {
    private Long id;
    private String paymentCode;
    private Long bookingId;
    private Long userId;
    private BigDecimal amount;
    @Builder.Default
    private String currency = "VND";
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
    private List<TransactionAggregate> transactions = new ArrayList<>();

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
