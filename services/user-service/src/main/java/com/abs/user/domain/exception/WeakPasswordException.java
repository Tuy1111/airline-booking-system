package com.abs.user.domain.exception;

/** Raised when a raw password does not meet the domain's password-strength policy. */
public class WeakPasswordException extends DomainException {

    public WeakPasswordException(String message) {
        super(message);
    }
}
