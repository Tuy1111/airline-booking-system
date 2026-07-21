package com.abs.user.domain.exception;

import java.time.LocalDate;

/** Raised when a business operation requires a passport that is not expired, but it is. */
public class ExpiredPassportException extends DomainException {

    public ExpiredPassportException(LocalDate expiryDate) {
        super("Passport expired on " + expiryDate + " and can no longer be used or verified");
    }
}
