package com.abs.user.domain.vo;

import com.abs.user.domain.exception.InsufficientMilesException;
import com.abs.user.domain.exception.InvalidMilesAmountException;

/**
 * Value Object for a quantity of frequent-flyer miles.
 *
 * <p>Immutable arithmetic: {@link #plus} and {@link #minus} return new instances and the type can
 * never represent a negative amount. {@link #minus} additionally enforces the business rule that a
 * balance cannot be overdrawn, raising {@link InsufficientMilesException}.
 */
public record FrequentFlyerMiles(long value) {

    public static final FrequentFlyerMiles ZERO = new FrequentFlyerMiles(0L);

    public FrequentFlyerMiles {
        if (value < 0) {
            throw new InvalidMilesAmountException("Miles amount cannot be negative: " + value);
        }
    }

    public static FrequentFlyerMiles of(long value) {
        return new FrequentFlyerMiles(value);
    }

    public FrequentFlyerMiles plus(FrequentFlyerMiles other) {
        return new FrequentFlyerMiles(this.value + other.value);
    }

    public FrequentFlyerMiles minus(FrequentFlyerMiles other) {
        if (other.value > this.value) {
            throw new InsufficientMilesException(this.value, other.value);
        }
        return new FrequentFlyerMiles(this.value - other.value);
    }

    public boolean isZero() {
        return value == 0L;
    }
}
