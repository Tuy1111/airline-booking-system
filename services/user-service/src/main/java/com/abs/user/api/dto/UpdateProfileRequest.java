package com.abs.user.api.dto;

import com.abs.user.domain.vo.Gender;

import java.time.LocalDate;

/** Request body for updating a passenger profile (the user id comes from the path). */
public record UpdateProfileRequest(
        String fullName,
        String phone,
        LocalDate dateOfBirth,
        Gender gender,
        String nationality) {
}
