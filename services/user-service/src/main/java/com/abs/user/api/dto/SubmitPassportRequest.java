package com.abs.user.api.dto;

import java.time.LocalDate;

/** Request body for submitting a passport for KYC verification. */
public record SubmitPassportRequest(String passportNumber, String issuingCountry, LocalDate expiryDate) {
}
