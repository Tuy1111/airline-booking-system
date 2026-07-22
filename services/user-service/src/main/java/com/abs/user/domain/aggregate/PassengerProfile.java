package com.abs.user.domain.aggregate;

import com.abs.user.domain.exception.ExpiredPassportException;
import com.abs.user.domain.exception.InvalidPassportException;
import com.abs.user.domain.exception.InvalidPersonNameException;
import com.abs.user.domain.exception.KycVerificationException;
import com.abs.user.domain.vo.CountryCode;
import com.abs.user.domain.vo.DateOfBirth;
import com.abs.user.domain.vo.Gender;
import com.abs.user.domain.vo.KycStatus;
import com.abs.user.domain.vo.Passport;
import com.abs.user.domain.vo.PersonName;
import com.abs.user.domain.vo.PhoneNumber;

import java.time.LocalDate;

/**
 * Internal entity of the {@link UserAggregate}: the traveller profile (name, contact, identity
 * document) attached one-to-one to a registered account.
 *
 * <p><b>Why an internal entity and not its own aggregate?</b> A passenger profile in this bounded
 * context has no life of its own — it is created together with the account, deleted together with
 * the account, and is only ever consistent in the context of its owning {@code User}. It therefore
 * lives <em>inside</em> the User consistency boundary.
 *
 * <p><b>How it is protected:</b> every state-changing method is package-private, so only the
 * aggregate root (same package) can invoke them. Application code cannot reach in and mutate a
 * profile directly; it must go through an intention-revealing method on {@code UserAggregate}. The
 * only public factory, {@link #reconstitute}, exists for the persistence adapter to rehydrate a
 * stored profile.
 */
public class PassengerProfile {

    private PersonName fullName;
    private PhoneNumber phone;
    private DateOfBirth dateOfBirth;
    private Gender gender;
    private CountryCode nationality;
    private Passport passport;
    private KycStatus kycStatus;

    private PassengerProfile(PersonName fullName, PhoneNumber phone, DateOfBirth dateOfBirth,
                             Gender gender, CountryCode nationality, Passport passport,
                             KycStatus kycStatus) {
        this.fullName = fullName;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.nationality = nationality;
        this.passport = passport;
        this.kycStatus = kycStatus;
    }

    /** Creates the default profile that accompanies a freshly registered account. */
    static PassengerProfile create(PersonName fullName, PhoneNumber phone) {
        if (fullName == null) {
            throw new InvalidPersonNameException("Passenger full name is required");
        }
        return new PassengerProfile(fullName, phone, null, null, null, null, KycStatus.UNVERIFIED);
    }

    /** Rehydrates a profile from persisted state. Intended for the persistence adapter only. */
    public static PassengerProfile reconstitute(PersonName fullName, PhoneNumber phone,
                                                DateOfBirth dateOfBirth, Gender gender,
                                                CountryCode nationality, Passport passport,
                                                KycStatus kycStatus) {
        if (fullName == null) {
            throw new InvalidPersonNameException("Passenger full name is required");
        }
        return new PassengerProfile(fullName, phone, dateOfBirth, gender, nationality, passport,
                kycStatus == null ? KycStatus.UNVERIFIED : kycStatus);
    }

    // ----- state transitions (package-private: driven only by UserAggregate) -----

    void changeContactDetails(PhoneNumber phone) {
        this.phone = phone;
    }

    void changePersonalDetails(PersonName fullName, DateOfBirth dateOfBirth, Gender gender,
                               CountryCode nationality) {
        if (fullName == null) {
            throw new InvalidPersonNameException("Passenger full name is required");
        }
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.nationality = nationality;
    }

    /**
     * Records a newly submitted passport and moves the profile into KYC {@code PENDING}. An
     * already-expired document is rejected outright — there is no point queueing it for review.
     */
    void submitPassport(Passport passport, LocalDate asOf) {
        if (passport == null) {
            throw new InvalidPassportException("A passport is required to start verification");
        }
        if (passport.isExpired(asOf)) {
            throw new ExpiredPassportException(passport.expiryDate());
        }
        this.passport = passport;
        this.kycStatus = KycStatus.PENDING;
    }

    /** Approves the on-file passport. Invariant: a non-expired passport must be present. */
    void verifyKyc(LocalDate asOf) {
        if (passport == null) {
            throw new KycVerificationException("Cannot verify KYC: no passport on file");
        }
        if (passport.isExpired(asOf)) {
            throw new ExpiredPassportException(passport.expiryDate());
        }
        this.kycStatus = KycStatus.VERIFIED;
    }

    void rejectKyc() {
        if (passport == null) {
            throw new KycVerificationException("Cannot reject KYC: no passport was submitted");
        }
        this.kycStatus = KycStatus.REJECTED;
    }

    /** Strips all personally identifiable information as part of GDPR erasure. */
    void anonymize() {
        this.fullName = PersonName.of("REDACTED");
        this.phone = null;
        this.dateOfBirth = null;
        this.gender = null;
        this.nationality = null;
        this.passport = null;
        this.kycStatus = KycStatus.UNVERIFIED;
    }

    // ----- queries -----

    public boolean isKycVerified() {
        return kycStatus.isVerified();
    }

    public boolean hasValidPassport(LocalDate asOf) {
        return passport != null && !passport.isExpired(asOf);
    }

    public PersonName getFullName() {
        return fullName;
    }

    public PhoneNumber getPhone() {
        return phone;
    }

    public DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public CountryCode getNationality() {
        return nationality;
    }

    public Passport getPassport() {
        return passport;
    }

    public KycStatus getKycStatus() {
        return kycStatus;
    }
}
