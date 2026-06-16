package com.abs.booking.infrastructure.feign.dto;

import java.math.BigDecimal;

/**
 * DTO contract (consumer side): nhận data chuyến bay từ flight-search-service.
 * Phải khớp JSON với DemoFlightResponse bên flight-search-service.
 */
public record FlightResponse(
        Long id,
        String flightNo,
        String from,
        String to,
        String departureTime,
        String arrivalTime,
        BigDecimal price,
        int availableSeats,
        String airline
) {
}
