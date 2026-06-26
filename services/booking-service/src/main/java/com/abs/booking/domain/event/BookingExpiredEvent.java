package com.abs.booking.domain.event;

import java.time.LocalDateTime;

public record BookingExpiredEvent(
        Long bookingId,
        String bookingCode,
        Long userId,
        Long flightId,
        LocalDateTime expiredAt
) {}
