package com.abs.user.domain.exception;

/**
 * Raised when a KYC (Know-Your-Customer) state transition is not legal — e.g. verifying a profile
 * that has no passport on file, or approving a profile whose passport has already expired.
 */
public class KycVerificationException extends DomainException {

    public KycVerificationException(String message) {
        super(message);
    }
}
