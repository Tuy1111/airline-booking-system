package com.abs.flightsearch.application.dto;

import java.math.BigDecimal;

/**
 * DTO contract: chi tiết chuyến bay (có thêm status).
 */
public record FlightDetailInfo(
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
