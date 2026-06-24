package com.abs.user.domain.vo;

import com.abs.user.domain.exception.InvalidPassportException;

import java.time.LocalDate;

/**
 * Composite Value Object that bundles the three attributes that only make sense together — the
 * document number, the issuing country, and the expiry date — into a single concept.
 *
 * <p>It enforces structural completeness on construction and answers the domain-meaningful question
 * "is this passport expired?" itself, instead of leaking a raw {@code LocalDate} for callers to
 * compare. Note that constructing an expired passport is allowed (it can still be stored/displayed);
 * expiry is only rejected by the aggregate when the document is actually <em>used</em> or verified.
 */
public record Passport(PassportNumber number, CountryCode issuingCountry, LocalDate expiryDate) {

    public Passport {
        if (number == null) {
            throw new InvalidPassportException("Passport number is required");
        }
        if (issuingCountry == null) {
            throw new InvalidPassportException("Passport issuing country is required");
        }
        if (expiryDate == null) {
            throw new InvalidPassportException("Passport expiry date is required");
        }
    }

    public static Passport of(PassportNumber number, CountryCode issuingCountry, LocalDate expiryDate) {
        return new Passport(number, issuingCountry, expiryDate);
    }

    /** A passport is expired once its expiry date is on or before the reference date. */
    public boolean isExpired(LocalDate asOf) {
        return !expiryDate.isAfter(asOf);
    }

    public boolean isExpired() {
        return isExpired(LocalDate.now());
    }
}
