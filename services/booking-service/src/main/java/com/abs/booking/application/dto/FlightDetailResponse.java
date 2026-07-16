package com.abs.booking.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightDetailResponse(
        Long id,
        String flightNo,
        String airlineCode,
        String airlineName,
        String fromAirport,
        String fromCity,
        String toAirport,
        String toCity,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        BigDecimal basePrice,
        BigDecimal currentPrice,
        String aircraftType,
        String status,
        int availableSeats,
        int totalSeats,
        int heldSeats,
        int bookedSeats
) {}
