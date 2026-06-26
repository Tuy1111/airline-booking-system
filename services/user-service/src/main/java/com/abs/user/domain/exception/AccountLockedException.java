package com.abs.user.domain.exception;

/** Raised when authentication or a protected operation is attempted on a locked account. */
public class AccountLockedException extends DomainException {

    public AccountLockedException() {
        super("Account is locked due to too many failed login attempts and must be unlocked first");
    }
}
