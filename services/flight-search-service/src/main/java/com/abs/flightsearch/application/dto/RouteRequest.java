package com.abs.flightsearch.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record RouteRequest(
        @NotBlank String fromAirport,
        @NotBlank String toAirport,
        @Positive Integer distanceKm
) {}
