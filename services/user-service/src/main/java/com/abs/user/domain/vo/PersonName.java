package com.abs.user.domain.vo;

import com.abs.user.domain.exception.InvalidPersonNameException;

/**
 * Value Object for a person's full name. Trims surrounding whitespace, collapses internal runs of
 * whitespace to a single space, and guarantees a non-blank value within the persistable length.
 */
public record PersonName(String value) {

    private static final int MAX_LENGTH = 100;

    public PersonName {
        if (value == null || value.isBlank()) {
            throw new InvalidPersonNameException("Name must not be blank");
        }
        value = value.trim().replaceAll("\\s+", " ");
        if (value.length() > MAX_LENGTH) {
            throw new InvalidPersonNameException("Name must not exceed " + MAX_LENGTH + " characters");
        }
    }

    public static PersonName of(String value) {
        return new PersonName(value);
    }
}
