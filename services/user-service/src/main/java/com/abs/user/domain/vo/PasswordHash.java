package com.abs.user.domain.vo;

/**
 * Value Object wrapping an <em>already hashed</em> credential.
 *
 * <p>The domain deliberately never sees a raw password: hashing is an infrastructure concern
 * This legacy persistence value is retained for existing rows while authentication is managed by
 * Keycloak. The VO only guarantees that a stored
 * credential is present and within the persistable length, and it refuses to leak the digest via
 * {@link #toString()} so the secret never accidentally ends up in a log line.
 */
public record PasswordHash(String value) {

    private static final int MAX_LENGTH = 255;

    public PasswordHash {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Password hash must not be blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Password hash exceeds " + MAX_LENGTH + " characters");
        }
    }

    public static PasswordHash of(String hashedValue) {
        return new PasswordHash(hashedValue);
    }

    @Override
    public String toString() {
        return "PasswordHash[PROTECTED]";
    }
}
