package com.abs.flightsearch.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightUpdateRequest(
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        BigDecimal basePrice,
        String aircraftType,
        Integer totalSeats
) {}
