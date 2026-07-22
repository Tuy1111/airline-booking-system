package com.abs.user.domain.exception;

/**
 * Raised when an operation is attempted against an account that has been erased (GDPR right to be
 * forgotten). A deleted account is a terminal state: it can neither authenticate nor be mutated.
 */
public class AccountDeletedException extends DomainException {

    public AccountDeletedException(String operation) {
        super("Operation '" + operation + "' is not allowed: the account has been permanently deleted");
    }
}
