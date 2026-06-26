package com.abs.user.domain.vo;

/**
 * Identity Value Object for the {@code User} aggregate root.
 *
 * <p>Wrapping the raw {@code Long} primary key in a typed identity removes "primitive obsession" at
 * the boundaries: a method that needs a user id can no longer be handed an arbitrary {@code Long}
 * (a flight id, a booking id, ...) by mistake — the compiler enforces the distinction.
 */
public record UserId(Long value) {

    public UserId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("UserId must be a positive number, was: " + value);
        }
    }

    public static UserId of(Long value) {
        return new UserId(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
