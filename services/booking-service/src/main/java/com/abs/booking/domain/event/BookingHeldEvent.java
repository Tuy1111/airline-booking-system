package com.abs.booking.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingHeldEvent(
        Long bookingId,
        String bookingCode,
        Long userId,
        Long flightId,
        BigDecimal totalAmount,
        String currency,
        LocalDateTime heldAt,
        LocalDateTime expiresAt
) {}
