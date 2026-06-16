package com.abs.flightsearch.application.dto;

import java.math.BigDecimal;

/**
 * DTO contract: thông tin chuyến bay trả về khi search.
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
