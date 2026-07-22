package com.abs.user.domain.vo;

import com.abs.user.domain.exception.InvalidPhoneNumberException;

import java.util.regex.Pattern;

/**
 * Value Object for a contact phone number.
 *
 * <p>Whitespace and dashes are stripped on construction so two phone numbers that differ only in
 * formatting compare equal. The accepted shape is an optional leading {@code +} followed by 8-15
 * digits (loosely E.164), which also fits the 20-character persistence column.
 */
public record PhoneNumber(String value) {

    private static final Pattern PATTERN = Pattern.compile("^\\+?[0-9]{8,15}$");

    public PhoneNumber {
        if (value == null) {
            throw new InvalidPhoneNumberException("null");
        }
        value = value.replaceAll("[\\s-]", "");
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidPhoneNumberException(value);
        }
    }

    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }
}
