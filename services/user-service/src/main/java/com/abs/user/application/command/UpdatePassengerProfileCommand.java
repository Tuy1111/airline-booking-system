package com.abs.user.application.command;

import com.abs.user.domain.vo.Gender;

import java.time.LocalDate;

/** Input for updating a passenger profile (UC: Update Passenger Profile). */
public record UpdatePassengerProfileCommand(
        Long userId,
        String fullName,
        String phone,
        LocalDate dateOfBirth,
        Gender gender,
        String nationality) {
}
