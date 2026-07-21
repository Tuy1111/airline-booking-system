package com.abs.booking.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HoldSeatResponse(
        Long bookingId,
        String bookingCode,
        Long flightId,
        String seatNo,
        BigDecimal price,
        Integer baggageWeightKg,
        BigDecimal baggageFee,
        String currency,
        LocalDateTime holdExpiresAt,
        String message
) {
    public static HoldSeatResponse of(Long bookingId, String bookingCode, Long flightId,
                                       String seatNo, BigDecimal price, Integer baggageWeightKg,
                                       BigDecimal baggageFee, String currency,
                                       LocalDateTime holdExpiresAt) {
        return new HoldSeatResponse(bookingId, bookingCode, flightId, seatNo, price, baggageWeightKg, baggageFee, currency,
                holdExpiresAt, "Seat held successfully. Please complete payment before expiry.");
    }
}

