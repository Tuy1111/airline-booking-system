package com.abs.notification.application.dto;

import java.math.BigDecimal;

public record BookingConfirmedEvent(
        String bookingCode,
        Long userId,
        String passengerName,
        String recipientEmail,
        String flightNo,
        String from,
        String to,
        String departureTime,
        String seatNo,
        BigDecimal amount,
        String currency
) {}
