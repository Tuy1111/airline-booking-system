package com.abs.payment.application.dto;

import com.abs.payment.domain.Payment;
import com.abs.payment.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        String paymentCode,
        Long bookingId,
        BigDecimal amount,
        String currency,
        PaymentStatus status,
        String gateway,
        String transferCode,
        String qrUrl,
        LocalDateTime expiresAt
) {
    public static PaymentResponse of(Payment p, String qrUrl) {
        return new PaymentResponse(
                p.getId(), p.getPaymentCode(), p.getBookingId(),
                p.getAmount(), p.getCurrency(), p.getStatus(),
                p.getGateway() == null ? null : p.getGateway().name(),
                p.getTransferCode(), qrUrl, p.getExpiresAt()
        );
    }
}
