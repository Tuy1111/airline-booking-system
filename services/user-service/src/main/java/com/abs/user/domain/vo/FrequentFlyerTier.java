package com.abs.user.domain.vo;

/**
 * Frequent-flyer status tier, modelled as a rich enum that owns its own qualification thresholds.
 *
 * <p>Tiers are ordered (declaration order = ascending privilege). The qualifying threshold is
 * expressed in <em>lifetime</em> miles — i.e. status is driven by miles ever earned, not by the
 * spendable balance, which mirrors how real airline programmes separate "status miles" from
 * "award miles".
 */
public enum FrequentFlyerTier {

    BLUE(0L),
    SILVER(25_000L),
    GOLD(50_000L),
    PLATINUM(100_000L);

    private final long qualifyingMiles;

    FrequentFlyerTier(long qualifyingMiles) {
        this.qualifyingMiles = qualifyingMiles;
    }

    public long qualifyingMiles() {
        return qualifyingMiles;
    }

    /** Resolves the highest tier earned for a given amount of lifetime miles. */
    public static FrequentFlyerTier forLifetimeMiles(FrequentFlyerMiles lifetimeMiles) {
        FrequentFlyerTier earned = BLUE;
        for (FrequentFlyerTier tier : values()) {
            if (lifetimeMiles.value() >= tier.qualifyingMiles) {
                earned = tier;
            }
        }
        return earned;
    }

    public boolean isHigherThan(FrequentFlyerTier other) {
        return this.ordinal() > other.ordinal();
    }
}
