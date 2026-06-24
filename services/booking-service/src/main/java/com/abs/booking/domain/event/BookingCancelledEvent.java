package com.abs.booking.domain.event;

import java.time.LocalDateTime;

public record BookingCancelledEvent(
        Long bookingId,
        String bookingCode,
        Long userId,
        Long flightId,
        String reason,
        LocalDateTime cancelledAt
) {}
