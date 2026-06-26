package com.abs.user.domain.vo;

import com.abs.user.domain.exception.InvalidPassportException;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Value Object for a passport document number: 5-20 upper-case alphanumeric characters. Normalised
 * to upper-case on construction so equality is not defeated by letter casing.
 */
public record PassportNumber(String value) {

    private static final Pattern PATTERN = Pattern.compile("^[A-Z0-9]{5,20}$");

    public PassportNumber {
        if (value == null) {
            throw new InvalidPassportException("Passport number must not be null");
        }
        value = value.trim().toUpperCase(Locale.ROOT);
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidPassportException(
                    "Passport number must be 5-20 alphanumeric characters, was: '" + value + "'");
        }
    }

    public static PassportNumber of(String value) {
        return new PassportNumber(value);
    }
}
