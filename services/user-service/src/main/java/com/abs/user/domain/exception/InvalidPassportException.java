package com.abs.user.domain.exception;

/** Raised when passport data is structurally invalid (bad number format, missing fields, ...). */
public class InvalidPassportException extends DomainException {

    public InvalidPassportException(String message) {
        super(message);
    }
}
