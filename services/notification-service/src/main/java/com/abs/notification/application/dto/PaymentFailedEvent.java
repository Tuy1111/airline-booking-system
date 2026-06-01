package com.abs.notification.application.dto;

public record PaymentFailedEvent(
        String bookingCode,
        Long userId,
        String passengerName,
        String recipientEmail,
        String reason
) {}
