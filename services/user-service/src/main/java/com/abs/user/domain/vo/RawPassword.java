package com.abs.user.domain.vo;

import com.abs.user.domain.exception.WeakPasswordException;

/**
 * Transient Value Object representing a plaintext password supplied by a user, used only long enough
 * to be validated against the strength policy and handed to the hasher.
 *
 * <p>It is never persisted and never logged ({@link #toString()} is masked). Keeping the strength
 * policy here — rather than scattered across controllers — means "what counts as an acceptable
 * password" is a single, testable domain rule.
 */
public record RawPassword(String value) {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 72; // practical cap (also the bcrypt input limit)

    public RawPassword {
        if (value == null || value.length() < MIN_LENGTH) {
            throw new WeakPasswordException("Password must be at least " + MIN_LENGTH + " characters");
        }
        if (value.length() > MAX_LENGTH) {
            throw new WeakPasswordException("Password must not exceed " + MAX_LENGTH + " characters");
        }
        boolean hasLetter = value.chars().anyMatch(Character::isLetter);
        boolean hasDigit = value.chars().anyMatch(Character::isDigit);
        if (!hasLetter || !hasDigit) {
            throw new WeakPasswordException("Password must contain both letters and digits");
        }
    }

    public static RawPassword of(String value) {
        return new RawPassword(value);
    }

    @Override
    public String toString() {
        return "RawPassword[PROTECTED]";
    }
}
