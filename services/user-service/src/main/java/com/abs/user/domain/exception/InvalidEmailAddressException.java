package com.abs.user.domain.exception;

/** Raised when a string cannot be accepted as a well-formed {@code EmailAddress}. */
public class InvalidEmailAddressException extends DomainException {

    public InvalidEmailAddressException(String rawValue) {
        super("Invalid email address: '" + rawValue + "'");
    }
}
