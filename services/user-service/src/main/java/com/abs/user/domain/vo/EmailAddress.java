package com.abs.user.domain.vo;

import com.abs.user.domain.exception.InvalidEmailAddressException;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Value Object representing a syntactically valid, canonicalised email address.
 *
 * <p>Immutable and self-validating: an {@code EmailAddress} instance can only ever exist if the
 * supplied string passed format validation, so the rest of the system never has to re-check it.
 * The value is normalised to lower-case so that equality / uniqueness checks are case-insensitive.
 */
public record EmailAddress(String value) {

    private static final Pattern PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final int MAX_LENGTH = 100;

    public EmailAddress {
        if (value == null) {
            throw new InvalidEmailAddressException("null");
        }
        value = value.trim().toLowerCase(Locale.ROOT);
        if (value.length() > MAX_LENGTH || !PATTERN.matcher(value).matches()) {
            throw new InvalidEmailAddressException(value);
        }
    }

    public static EmailAddress of(String value) {
        return new EmailAddress(value);
    }
}
