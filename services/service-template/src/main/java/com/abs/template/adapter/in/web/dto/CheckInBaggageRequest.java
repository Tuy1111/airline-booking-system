package com.abs.template.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * Request JSON của API — hợp đồng với client, tách khỏi {@code CheckInBaggageCommand}
 * của tầng port. Validation cú pháp (Bean Validation) nằm ở đây; validation nghiệp vụ
 * nằm trong domain.
 */
public record CheckInBaggageRequest(
        @NotBlank String bookingRef,
        @NotNull @PositiveOrZero BigDecimal weightKg,
        @NotNull @Positive BigDecimal allowanceKg
) {
}
