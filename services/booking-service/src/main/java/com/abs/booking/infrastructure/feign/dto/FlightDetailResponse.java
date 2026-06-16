package com.abs.booking.infrastructure.feign.dto;

import java.math.BigDecimal;

/**
 * DTO contract (consumer side): chi tiết chuyến bay từ flight-search-service.
 * Phải khớp JSON với DemoFlightDetailResponse bên flight-search-service.
 */
public record FlightDetailResponse(
        Long id,
        String flightNo,
        String from,
        String to,
        String departureTime,
        String arrivalTime,
        BigDecimal price,
        int availableSeats,
        String airline,
        String status
) {
}
