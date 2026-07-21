package com.abs.notification.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SendNotificationRequest(
        @NotNull @Positive Long userId,
        @NotBlank @Size(max = 255) String title,
        @NotBlank String content,
        @NotBlank @Size(max = 50) String type) {
}
