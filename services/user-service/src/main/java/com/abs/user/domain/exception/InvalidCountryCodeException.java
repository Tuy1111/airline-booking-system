package com.abs.user.domain.exception;

/** Raised when a string is not a valid ISO 3166-1 alpha-3 country code. */
public class InvalidCountryCodeException extends DomainException {

    public InvalidCountryCodeException(String rawValue) {
        super("Invalid ISO-3166 alpha-3 country code: '" + rawValue + "'");
    }
}
