package com.abs.payment.domain.vo;

import java.math.BigDecimal;

/** Value Object: defined by attributes, immutable, equality by value (record auto-generates equals/hashCode). */
public record Money(BigDecimal amount, String currency) {
    public Money {
        if (amount == null) throw new IllegalArgumentException("Money: amount required");
        if (currency == null || currency.isBlank()) throw new IllegalArgumentException("Money: currency required");
        if (amount.signum() < 0) throw new IllegalArgumentException("Money cannot be negative");
    }
    public static Money of(BigDecimal amount, String currency) { return new Money(amount, currency); }
    public static Money vnd(BigDecimal amount) { return new Money(amount, "VND"); }
    public Money add(Money other) {
        if (!currency.equals(other.currency)) throw new IllegalArgumentException("Currency mismatch");
        return new Money(amount.add(other.amount), currency);
    }
}
