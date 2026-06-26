package com.abs.user.application.exception;

/**
 * Raised when authentication fails. Deliberately generic — it does not reveal whether the email was
 * unknown, the password was wrong, or the account was not active, to avoid user enumeration.
 */
public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException() {
        super("Invalid email or password");
    }
}
