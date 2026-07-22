package com.abs.user.domain.exception;

/** Raised when a string cannot be accepted as a valid {@code PersonName}. */
public class InvalidPersonNameException extends DomainException {

    public InvalidPersonNameException(String message) {
        super(message);
    }
}
