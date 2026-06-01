package com.abs.payment.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentCompletedEvent(
        Long       paymentId,
        String     paymentCode,
        Long       bookingId,
        Long       userId,
        BigDecimal amount,
        String     currency,
        String     gateway,
        String     referenceCode,
        LocalDateTime paidAt
) {}
