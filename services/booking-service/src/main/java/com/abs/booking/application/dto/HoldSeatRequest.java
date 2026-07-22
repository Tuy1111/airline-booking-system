package com.abs.booking.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HoldSeatRequest(
        @NotNull Long flightId,
        @NotBlank String seatNo,
        @NotBlank String passengerName,
        String passengerPassport,
        @Min(value = 0, message = "Số kg ký gửi phải lớn hơn hoặc bằng 0")
        @Max(value = 20, message = "Hành lý ký gửi tối đa 20kg")
        Integer extraBaggageKg
) {
}

