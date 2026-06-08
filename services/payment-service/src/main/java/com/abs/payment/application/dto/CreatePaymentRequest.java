package com.abs.payment.application.dto;

import com.abs.payment.domain.vo.PaymentMethod;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        @NotNull Long bookingId,
        @NotNull Long userId,
        @NotNull @DecimalMin("1000") BigDecimal amount,
        @NotBlank String idempotencyKey,
        PaymentMethod method   // optional; defaults to BANK_TRANSFER for SePay
) {}
