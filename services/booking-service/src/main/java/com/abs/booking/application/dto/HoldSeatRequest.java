package com.abs.booking.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HoldSeatRequest(
        @NotNull Long flightId,
        @NotBlank String seatNo,
        @NotBlank String passengerName,
        String passengerPassport
) {
}
