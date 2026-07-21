package com.abs.user.domain.vo;

import java.time.LocalDate;
import java.time.Period;

/**
 * Value Object for a date of birth. Enforces the temporal invariants that a birth date cannot be in
 * the future and cannot be unrealistically old, and exposes age-related domain queries so callers
 * never reimplement age arithmetic.
 */
public record DateOfBirth(LocalDate value) {

    private static final int MAX_AGE_YEARS = 120;

    public DateOfBirth {
        if (value == null) {
            throw new IllegalArgumentException("Date of birth must not be null");
        }
        LocalDate today = LocalDate.now();
        if (value.isAfter(today)) {
            throw new IllegalArgumentException("Date of birth cannot be in the future: " + value);
        }
        if (value.isBefore(today.minusYears(MAX_AGE_YEARS))) {
            throw new IllegalArgumentException("Date of birth is unrealistically old: " + value);
        }
    }

    public static DateOfBirth of(LocalDate value) {
        return new DateOfBirth(value);
    }

    public int ageInYears() {
        return Period.between(value, LocalDate.now()).getYears();
    }

    public boolean isMinor() {
        return ageInYears() < 18;
    }
}
