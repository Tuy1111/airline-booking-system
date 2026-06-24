package com.abs.user.domain.vo;

import com.abs.user.domain.exception.InvalidCountryCodeException;

import java.util.Locale;
import java.util.Set;

/**
 * Value Object for an ISO 3166-1 alpha-3 country code (e.g. {@code VNM}, {@code USA}).
 *
 * <p>Membership is validated against the JDK's authoritative ISO country list, so an instance can
 * never hold a made-up code. Used both for a passenger's nationality and a passport's issuing
 * country.
 */
public record CountryCode(String value) {

    private static final Set<String> ISO_ALPHA3 =
            Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA3);

    public CountryCode {
        if (value == null) {
            throw new InvalidCountryCodeException("null");
        }
        value = value.trim().toUpperCase(Locale.ROOT);
        if (!ISO_ALPHA3.contains(value)) {
            throw new InvalidCountryCodeException(value);
        }
    }

    public static CountryCode of(String value) {
        return new CountryCode(value);
    }
}
