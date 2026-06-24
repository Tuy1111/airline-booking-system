package com.abs.notification.application.dto;

public record BookingCancelledEvent(
        String bookingCode,
        Long userId,
        String passengerName,
        String recipientEmail,
        String reason
) {}
