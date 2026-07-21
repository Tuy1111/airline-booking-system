package com.abs.user.domain.exception;

/**
 * Base type for every business-rule / invariant violation raised inside the domain layer.
 *
 * <p>The domain layer never throws framework- or persistence-specific exceptions. It speaks only in
 * terms of {@code DomainException} subclasses so that outer layers (application, API) can translate
 * a violated invariant into the appropriate transport concern (HTTP status, message, etc.) without
 * the domain knowing anything about them.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
