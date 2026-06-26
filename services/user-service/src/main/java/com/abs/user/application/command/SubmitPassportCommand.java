package com.abs.user.application.command;

import java.time.LocalDate;

/** Input for submitting a passport for KYC verification (UC: KYC / Passport Verification). */
public record SubmitPassportCommand(
        Long userId,
        String passportNumber,
        String issuingCountry,
        LocalDate expiryDate) {
}
