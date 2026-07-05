package com.abs.flightsearch.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AirlineRequest(
        @NotBlank @Size(min = 2, max = 2) String code,
        @NotBlank String name
) {}
