package com.abs.user.domain.aggregate;

import com.abs.user.domain.exception.InvalidMilesAmountException;
import com.abs.user.domain.vo.FrequentFlyerMiles;
import com.abs.user.domain.vo.FrequentFlyerTier;

import java.time.LocalDateTime;

/**
 * Internal entity of the {@link UserAggregate}: the member's frequent-flyer standing.
 *
 * <p>It keeps two distinct miles figures — a spendable {@code milesBalance} and a monotonically
 * increasing {@code lifetimeMiles}. Tier (status) is derived solely from lifetime miles, mirroring
 * how real loyalty programmes separate "award miles" you can redeem from "status miles" that decide
 * your tier. Redeeming therefore never demotes a member.
 *
 * <p>Like {@link PassengerProfile}, all mutators are package-private so the standing can only ever
 * be changed through the aggregate root, keeping the tier/balance invariants enforced in one place.
 */
public class LoyaltyMembership {

    private FrequentFlyerTier tier;
    private FrequentFlyerMiles milesBalance;
    private FrequentFlyerMiles lifetimeMiles;
    private LocalDateTime enrolledAt;

    private LoyaltyMembership(FrequentFlyerTier tier, FrequentFlyerMiles milesBalance,
                             FrequentFlyerMiles lifetimeMiles, LocalDateTime enrolledAt) {
        this.tier = tier;
        this.milesBalance = milesBalance;
        this.lifetimeMiles = lifetimeMiles;
        this.enrolledAt = enrolledAt;
    }

    /** Enrols a brand-new member at the entry ({@code BLUE}) tier with a zero balance. */
    static LoyaltyMembership enroll(LocalDateTime enrolledAt) {
        return new LoyaltyMembership(FrequentFlyerTier.BLUE, FrequentFlyerMiles.ZERO,
                FrequentFlyerMiles.ZERO, enrolledAt);
    }

    /** Rehydrates a membership from persisted state. Intended for the persistence adapter only. */
    public static LoyaltyMembership reconstitute(FrequentFlyerTier tier, FrequentFlyerMiles milesBalance,
                                                 FrequentFlyerMiles lifetimeMiles,
                                                 LocalDateTime enrolledAt) {
        return new LoyaltyMembership(
                tier == null ? FrequentFlyerTier.BLUE : tier,
                milesBalance == null ? FrequentFlyerMiles.ZERO : milesBalance,
                lifetimeMiles == null ? FrequentFlyerMiles.ZERO : lifetimeMiles,
                enrolledAt);
    }

    // ----- state transitions (package-private: driven only by UserAggregate) -----

    /**
     * Credits earned miles to both the spendable balance and the lifetime total, then recomputes
     * the tier.
     *
     * @return {@code true} if the credit pushed the member into a higher tier
     */
    boolean earn(FrequentFlyerMiles miles) {
        requirePositive(miles, "earn");
        this.milesBalance = this.milesBalance.plus(miles);
        this.lifetimeMiles = this.lifetimeMiles.plus(miles);
        FrequentFlyerTier recalculated = FrequentFlyerTier.forLifetimeMiles(this.lifetimeMiles);
        boolean upgraded = recalculated.isHigherThan(this.tier);
        this.tier = recalculated;
        return upgraded;
    }

    /** Debits the spendable balance. Throws {@code InsufficientMilesException} if overdrawn. */
    void redeem(FrequentFlyerMiles miles) {
        requirePositive(miles, "redeem");
        this.milesBalance = this.milesBalance.minus(miles);
    }

    /** Clears loyalty standing (used during GDPR erasure). */
    void reset() {
        this.tier = FrequentFlyerTier.BLUE;
        this.milesBalance = FrequentFlyerMiles.ZERO;
        this.lifetimeMiles = FrequentFlyerMiles.ZERO;
    }

    private static void requirePositive(FrequentFlyerMiles miles, String operation) {
        if (miles == null || miles.isZero()) {
            throw new InvalidMilesAmountException("Must " + operation + " a positive number of miles");
        }
    }

    // ----- queries -----

    public FrequentFlyerTier getTier() {
        return tier;
    }

    public FrequentFlyerMiles getMilesBalance() {
        return milesBalance;
    }

    public FrequentFlyerMiles getLifetimeMiles() {
        return lifetimeMiles;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }
}
