package com.abs.booking.application.dto;

/**
 * Request DTO: client gửi lên booking-service để tìm & giữ chỗ.
 */
public record SearchFlightRequest(
        String from,
        String to,
        String date
) {
}
