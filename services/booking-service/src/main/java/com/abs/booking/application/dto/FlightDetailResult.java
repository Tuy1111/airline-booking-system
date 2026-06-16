package com.abs.booking.application.dto;

import com.abs.booking.infrastructure.feign.dto.FlightDetailResponse;

/**
 * Response DTO: booking-service trả về chi tiết chuyến bay cho client.
 */
public record FlightDetailResult(
        String source,
        FlightDetailResponse flightDetail
) {
}
