package com.abs.user.domain.exception;

/** Raised when a member tries to redeem more miles than the current balance allows. */
public class InsufficientMilesException extends DomainException {

    public InsufficientMilesException(long balance, long requested) {
        super("Cannot redeem " + requested + " miles; current balance is only " + balance);
    }
}
