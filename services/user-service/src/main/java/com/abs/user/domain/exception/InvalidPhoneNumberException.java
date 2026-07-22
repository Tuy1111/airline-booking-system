package com.abs.user.domain.exception;

/** Raised when a string cannot be accepted as a well-formed {@code PhoneNumber}. */
public class InvalidPhoneNumberException extends DomainException {

    public InvalidPhoneNumberException(String rawValue) {
        super("Invalid phone number: '" + rawValue + "'");
    }
}
