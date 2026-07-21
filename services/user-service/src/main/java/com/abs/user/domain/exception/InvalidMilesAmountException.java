package com.abs.user.domain.exception;

/** Raised when an attempt is made to construct or apply a non-positive / illegal miles amount. */
public class InvalidMilesAmountException extends DomainException {

    public InvalidMilesAmountException(String message) {
        super(message);
    }
}
