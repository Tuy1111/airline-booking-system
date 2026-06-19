package com.abs.payment.infrastructure.persistence.entity;

import com.abs.payment.domain.enums.PaymentGateway;
import com.abs.payment.domain.enums.PaymentMethod;
import com.abs.payment.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_code", nullable = false, unique = true, length = 30)
    private String paymentCode;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;       // logical ref → booking-service

    @Column(name = "user_id", nullable = false)
    private Long userId;          // logical ref → user-service

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    @Builder.Default
    private String currency = "VND";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentGateway gateway;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "idempotency_key", nullable = false, unique = true, length = 100)
    private String idempotencyKey;

    /** Mã ngắn user phải nhập vào nội dung CK để hệ thống đối soát. UNIQUE per active payment. */
    @Column(name = "transfer_code", length = 30)
    private String transferCode;

    /** Mã giao dịch do SePay/ngân hàng trả về — dùng cho idempotency. */
    @Column(name = "reference_code", length = 100)
    private String referenceCode;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "failure_reason", length = 500)
    private String failureReason;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TransactionEntity> transactions = new ArrayList<>();
}
