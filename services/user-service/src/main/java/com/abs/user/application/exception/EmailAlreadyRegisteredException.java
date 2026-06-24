package com.abs.user.application.exception;

/** Raised by the registration use case when the requested email already belongs to an account. */
public class EmailAlreadyRegisteredException extends RuntimeException {

    public EmailAlreadyRegisteredException(String email) {
        super("Email already registered: " + email);
    }
}
