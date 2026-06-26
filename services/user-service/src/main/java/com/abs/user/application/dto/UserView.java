package com.abs.user.application.dto;

import com.abs.user.domain.aggregate.LoyaltyMembership;
import com.abs.user.domain.aggregate.PassengerProfile;
import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.vo.Passport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Read model / projection of the {@link UserAggregate} for returning to callers.
 *
 * <p>It flattens the aggregate's Value Objects back into transport-friendly primitives in a single
 * place ({@link #from}), so the rich domain types never leak out of the application boundary and the
 * unwrapping logic is not duplicated across controllers.
 */
public record UserView(
        Long id,
        String email,
        String status,
        Set<String> roles,
        String fullName,
        String phone,
        LocalDate dateOfBirth,
        String gender,
        String nationality,
        String passportNumber,
        String passportCountry,
        LocalDate passportExpiry,
        String kycStatus,
        String loyaltyTier,
        long milesBalance,
        long lifetimeMiles,
        LocalDateTime createdAt,
        LocalDateTime lastLoginAt) {

    public static UserView from(UserAggregate user) {
        PassengerProfile profile = user.getPassengerProfile();
        LoyaltyMembership loyalty = user.getLoyalty();
        Passport passport = profile.getPassport();

        return new UserView(
                user.getId() == null ? null : user.getId().value(),
                user.getEmail().value(),
                user.getStatus().name(),
                user.getRoles().stream().map(Enum::name).collect(Collectors.toUnmodifiableSet()),
                profile.getFullName().value(),
                profile.getPhone() == null ? null : profile.getPhone().value(),
                profile.getDateOfBirth() == null ? null : profile.getDateOfBirth().value(),
                profile.getGender() == null ? null : profile.getGender().name(),
                profile.getNationality() == null ? null : profile.getNationality().value(),
                passport == null ? null : passport.number().value(),
                passport == null ? null : passport.issuingCountry().value(),
                passport == null ? null : passport.expiryDate(),
                profile.getKycStatus().name(),
                loyalty.getTier().name(),
                loyalty.getMilesBalance().value(),
                loyalty.getLifetimeMiles().value(),
                user.getCreatedAt(),
                user.getLastLoginAt());
    }
}
