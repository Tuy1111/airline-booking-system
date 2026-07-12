package com.abs.user.infrastructure.persistence.mapper;

import com.abs.user.domain.aggregate.LoyaltyMembership;
import com.abs.user.domain.aggregate.PassengerProfile;
import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.vo.CountryCode;
import com.abs.user.domain.vo.DateOfBirth;
import com.abs.user.domain.vo.EmailAddress;
import com.abs.user.domain.vo.FrequentFlyerMiles;
import com.abs.user.domain.vo.KycStatus;
import com.abs.user.domain.vo.Passport;
import com.abs.user.domain.vo.PassportNumber;
import com.abs.user.domain.vo.PasswordHash;
import com.abs.user.domain.vo.PersonName;
import com.abs.user.domain.vo.PhoneNumber;
import com.abs.user.domain.vo.UserId;
import com.abs.user.infrastructure.persistence.entity.PassengerEntity;
import com.abs.user.infrastructure.persistence.entity.UserEntity;

import java.util.HashSet;

/**
 * Anti-corruption mapper between the rich {@link UserAggregate} and the flat JPA {@link UserEntity}.
 *
 * <p>This is the one place that knows how to turn primitive persisted columns back into validated
 * Value Objects and reassemble the aggregate via its {@code reconstitute} factories, and vice-versa.
 * Keeping it here means the domain never imports JPA and the entities never import domain behaviour.
 */
public final class UserPersistenceMapper {

    private UserPersistenceMapper() {
    }

    // ----- entity -> aggregate -----

    public static UserAggregate toAggregate(UserEntity entity) {
        LoyaltyMembership loyalty = LoyaltyMembership.reconstitute(
                entity.getLoyaltyTier(),
                FrequentFlyerMiles.of(entity.getLoyaltyMiles()),
                FrequentFlyerMiles.of(entity.getLoyaltyLifetimeMiles()),
                entity.getEnrolledAt());

        return UserAggregate.reconstitute(
                entity.getId() == null ? null : UserId.of(entity.getId()),
                EmailAddress.of(entity.getEmail()),
                PasswordHash.of(entity.getPasswordHash()),
                entity.getStatus(),
                entity.getRoles(),
                entity.getCreatedAt(),
                entity.getLastLoginAt(),
                entity.getFailedLoginAttempts(),
                entity.getDeletedAt(),
                toProfile(entity.getPassenger()),
                loyalty);
    }

    private static PassengerProfile toProfile(PassengerEntity entity) {
        if (entity == null) {
            // Defensive: a user row without a passenger row gets a minimal placeholder so the
            // aggregate invariant "every user has a profile" still holds after rehydration.
            return PassengerProfile.reconstitute(
                    PersonName.of("Unknown"), null, null, null, null, null, KycStatus.UNVERIFIED);
        }

        Passport passport = null;
        if (!isBlank(entity.getPassportNo())
                && !isBlank(entity.getPassportCountry())
                && entity.getPassportExpiry() != null) {
            passport = Passport.of(
                    PassportNumber.of(entity.getPassportNo()),
                    CountryCode.of(entity.getPassportCountry()),
                    entity.getPassportExpiry());
        }

        return PassengerProfile.reconstitute(
                PersonName.of(entity.getFullName()),
                isBlank(entity.getPhone()) ? null : PhoneNumber.of(entity.getPhone()),
                entity.getDateOfBirth() == null ? null : DateOfBirth.of(entity.getDateOfBirth()),
                entity.getGender(),
                isBlank(entity.getNationality()) ? null : CountryCode.of(entity.getNationality()),
                passport,
                entity.getKycStatus() == null ? KycStatus.UNVERIFIED : entity.getKycStatus());
    }

    // ----- aggregate -> entity -----

    public static UserEntity toEntity(UserAggregate aggregate) {
        LoyaltyMembership loyalty = aggregate.getLoyalty();

        UserEntity entity = UserEntity.builder()
                .id(aggregate.getId() == null ? null : aggregate.getId().value())
                .email(aggregate.getEmail().value())
                .passwordHash(aggregate.getPasswordHash().value())
                .status(aggregate.getStatus())
                .createdAt(aggregate.getCreatedAt())
                .lastLoginAt(aggregate.getLastLoginAt())
                .failedLoginAttempts(aggregate.getFailedLoginAttempts())
                .deletedAt(aggregate.getDeletedAt())
                .loyaltyTier(loyalty.getTier())
                .loyaltyMiles(loyalty.getMilesBalance().value())
                .loyaltyLifetimeMiles(loyalty.getLifetimeMiles().value())
                .enrolledAt(loyalty.getEnrolledAt())
                .roles(new HashSet<>(aggregate.getRoles()))
                .build();

        entity.setPassenger(toPassengerEntity(aggregate.getPassengerProfile(), entity));
        return entity;
    }

    private static PassengerEntity toPassengerEntity(PassengerProfile profile, UserEntity owner) {
        Passport passport = profile.getPassport();
        return PassengerEntity.builder()
                .userId(owner.getId())
                .user(owner) // @MapsId derives user_id from the owning user
                .fullName(profile.getFullName().value())
                .phone(profile.getPhone() == null ? null : profile.getPhone().value())
                .dateOfBirth(profile.getDateOfBirth() == null ? null : profile.getDateOfBirth().value())
                .gender(profile.getGender())
                .passportNo(passport == null ? null : passport.number().value())
                .passportCountry(passport == null ? null : passport.issuingCountry().value())
                .passportExpiry(passport == null ? null : passport.expiryDate())
                .nationality(profile.getNationality() == null ? null : profile.getNationality().value())
                .kycStatus(profile.getKycStatus())
                .build();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
