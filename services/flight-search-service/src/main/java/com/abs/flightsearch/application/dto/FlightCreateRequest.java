package com.abs.flightsearch.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightCreateRequest(
        @NotBlank String flightNo,
        @NotBlank String fromAirportCode,
        @NotBlank String toAirportCode,
        @NotBlank String airlineCode,
        @NotNull LocalDateTime departureTime,
        @NotNull LocalDateTime arrivalTime,
        @NotNull @Positive Integer totalSeats,
        @NotNull BigDecimal basePrice,
        String aircraftType
) {}
