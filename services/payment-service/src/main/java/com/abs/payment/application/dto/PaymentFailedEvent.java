package com.abs.payment.application.dto;

import java.math.BigDecimal;

public record PaymentFailedEvent(
        Long       paymentId,
        String     paymentCode,
        Long       bookingId,
        Long       userId,
        BigDecimal amount,
        String     reason
) {}
