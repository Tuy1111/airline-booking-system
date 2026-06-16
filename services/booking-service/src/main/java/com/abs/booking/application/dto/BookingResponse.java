package com.abs.booking.application.dto;

import com.abs.booking.infrastructure.feign.dto.FlightResponse;

/**
 * Response DTO: booking-service trả về cho client sau khi search & hold.
 */
public record BookingResponse(
        String status,
        String bookingCode,
        String message,
        FlightResponse flight
) {
}
