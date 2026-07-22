package com.abs.flightsearch.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightImportRequest(
        String flightNo,
        String fromAirportCode,
        String fromAirportName,
        String fromAirportCity,
        String fromAirportCountry,
        String toAirportCode,
        String toAirportName,
        String toAirportCity,
        String toAirportCountry,
        Integer distanceKm,
        String airlineCode,
        String airlineName,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        Integer totalSeats,
        BigDecimal basePrice,
        String aircraftType
) {}
