package com.abs.user.domain.aggregate;

import com.abs.user.domain.exception.AccountDeletedException;
import com.abs.user.domain.exception.AccountLockedException;
import com.abs.user.domain.vo.CountryCode;
import com.abs.user.domain.vo.DateOfBirth;
import com.abs.user.domain.vo.EmailAddress;
import com.abs.user.domain.vo.FrequentFlyerMiles;
import com.abs.user.domain.vo.FrequentFlyerTier;
import com.abs.user.domain.vo.Gender;
import com.abs.user.domain.vo.Passport;
import com.abs.user.domain.vo.PasswordHash;
import com.abs.user.domain.vo.PersonName;
import com.abs.user.domain.vo.PhoneNumber;
import com.abs.user.domain.vo.Role;
import com.abs.user.domain.vo.UserId;
import com.abs.user.domain.vo.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Aggregate Root of the user-service.
 *
 * <p>A {@code UserAggregate} is the consistency boundary that bundles together everything that must
 * change atomically for one registered customer: their account credentials and lifecycle status,
 * their authorisation {@link Role roles}, their traveller {@link PassengerProfile profile}, and
 * their {@link LoyaltyMembership frequent-flyer standing}. The two child entities are reachable only
 * through this root and can only be mutated by it — outside code must call an intention-revealing
 * method here (e.g. {@link #earnMiles}, {@link #submitPassport}, {@link #requestErasure}) rather
 * than reaching into the internals. This is what keeps the invariants in one auditable place.
 *
 * <p>The class has <b>no dependency on any framework or persistence technology</b>; the only import
 * beyond the JDK and the domain itself is SLF4J, used for the mandated structured business logging.
 */
public class UserAggregate {

    private static final Logger log = LoggerFactory.getLogger(UserAggregate.class);

    /** Number of consecutive failed logins after which the account auto-locks. */
    public static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;

    private UserId id;
    private EmailAddress email;
    private PasswordHash passwordHash;
    private UserStatus status;
    private final Set<Role> roles;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private int failedLoginAttempts;
    private LocalDateTime deletedAt;

    private final PassengerProfile passengerProfile;
    private final LoyaltyMembership loyalty;

    private UserAggregate(UserId id, EmailAddress email, PasswordHash passwordHash, UserStatus status,
                          Set<Role> roles, LocalDateTime createdAt, LocalDateTime lastLoginAt,
                          int failedLoginAttempts, LocalDateTime deletedAt,
                          PassengerProfile passengerProfile, LoyaltyMembership loyalty) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.status = status;
        this.roles = roles;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
        this.failedLoginAttempts = failedLoginAttempts;
        this.deletedAt = deletedAt;
        this.passengerProfile = passengerProfile;
        this.loyalty = loyalty;
    }

    /**
     * Registers a brand-new account. Establishes the creation invariants: the account starts
     * {@code ACTIVE} with the {@code USER} role, a default passenger profile, and a {@code BLUE}
     * loyalty membership. Email uniqueness is a cross-aggregate rule enforced by the application
     * layer via the repository, not here.
     */
    public static UserAggregate register(EmailAddress email, PasswordHash passwordHash,
                                         PersonName fullName, PhoneNumber phone,
                                         LocalDateTime registeredAt) {
        return register(null, email, passwordHash, fullName, phone, registeredAt);
    }

    public static UserAggregate register(UserId id, EmailAddress email, PasswordHash passwordHash,
                                         PersonName fullName, PhoneNumber phone,
                                         LocalDateTime registeredAt) {
        Objects.requireNonNull(email, "email is required");
        Objects.requireNonNull(passwordHash, "passwordHash is required");
        Objects.requireNonNull(registeredAt, "registeredAt is required");

        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        PassengerProfile profile = PassengerProfile.create(fullName, phone);
        LoyaltyMembership loyalty = LoyaltyMembership.enroll(registeredAt);

        UserAggregate user = new UserAggregate(id, email, passwordHash, UserStatus.ACTIVE, roles,
                registeredAt, null, 0, null, profile, loyalty);
        log.info("User registered: email={}, status={}, role={}", email.value(), UserStatus.ACTIVE, Role.USER);
        return user;
    }

    /** Rehydrates a fully-formed aggregate from persisted state. For the persistence adapter only. */
    public static UserAggregate reconstitute(UserId id, EmailAddress email, PasswordHash passwordHash,
                                             UserStatus status, Set<Role> roles, LocalDateTime createdAt,
                                             LocalDateTime lastLoginAt, int failedLoginAttempts,
                                             LocalDateTime deletedAt, PassengerProfile passengerProfile,
                                             LoyaltyMembership loyalty) {
        return new UserAggregate(id, email, passwordHash, status,
                roles == null ? new HashSet<>() : new HashSet<>(roles),
                createdAt, lastLoginAt, failedLoginAttempts, deletedAt, passengerProfile, loyalty);
    }

    // ----- authentication lifecycle -----

    /** Records a successful authentication: clears the failure counter and stamps the login time. */
    public void recordSuccessfulLogin(LocalDateTime at) {
        ensureNotDeleted("login");
        if (status == UserStatus.LOCKED) {
            throw new AccountLockedException();
        }
        this.lastLoginAt = at;
        this.failedLoginAttempts = 0;
        log.info("Login succeeded: userId={}, at={}", idForLog(), at);
    }

    /** Records a failed authentication, auto-locking the account once the threshold is reached. */
    public void recordFailedLoginAttempt() {
        ensureNotDeleted("login");
        this.failedLoginAttempts++;
        log.warn("Login failed: userId={}, attempt={}/{}", idForLog(), failedLoginAttempts,
                MAX_FAILED_LOGIN_ATTEMPTS);
        if (this.failedLoginAttempts >= MAX_FAILED_LOGIN_ATTEMPTS && status == UserStatus.ACTIVE) {
            this.status = UserStatus.LOCKED;
            log.warn("Account locked after {} failed login attempts: userId={}",
                    failedLoginAttempts, idForLog());
        }
    }

    public void lock() {
        ensureNotDeleted("lock");
        this.status = UserStatus.LOCKED;
        log.info("Account locked: userId={}", idForLog());
    }

    public void unlock() {
        ensureNotDeleted("unlock");
        this.status = UserStatus.ACTIVE;
        this.failedLoginAttempts = 0;
        log.info("Account unlocked: userId={}", idForLog());
    }

    public void changePassword(PasswordHash newPasswordHash) {
        ensureNotDeleted("changePassword");
        this.passwordHash = Objects.requireNonNull(newPasswordHash, "newPasswordHash is required");
        log.info("Password changed: userId={}", idForLog());
    }

    // ----- passenger profile -----

    public void updateContactDetails(PhoneNumber phone) {
        ensureNotDeleted("updateContactDetails");
        passengerProfile.changeContactDetails(phone);
        log.info("Passenger contact details updated: userId={}", idForLog());
    }

    public void updatePersonalDetails(PersonName fullName, DateOfBirth dateOfBirth, Gender gender,
                                      CountryCode nationality) {
        ensureNotDeleted("updatePersonalDetails");
        passengerProfile.changePersonalDetails(fullName, dateOfBirth, gender, nationality);
        log.info("Passenger personal details updated: userId={}", idForLog());
    }

    // ----- KYC / passport verification -----

    public void submitPassport(Passport passport, LocalDate asOf) {
        ensureNotDeleted("submitPassport");
        passengerProfile.submitPassport(passport, asOf);
        log.info("Passport submitted for KYC: userId={}, issuingCountry={}, kycStatus={}",
                idForLog(), passport.issuingCountry().value(), passengerProfile.getKycStatus());
    }

    public void verifyPassport(LocalDate asOf) {
        ensureNotDeleted("verifyPassport");
        passengerProfile.verifyKyc(asOf);
        log.info("KYC verified: userId={}", idForLog());
    }

    public void rejectPassport() {
        ensureNotDeleted("rejectPassport");
        passengerProfile.rejectKyc();
        log.info("KYC rejected: userId={}", idForLog());
    }

    // ----- frequent-flyer loyalty -----

    public void earnMiles(FrequentFlyerMiles miles) {
        ensureNotDeleted("earnMiles");
        FrequentFlyerTier before = loyalty.getTier();
        boolean upgraded = loyalty.earn(miles);
        log.info("Miles earned: userId={}, amount={}, balance={}, lifetime={}, tier={}",
                idForLog(), miles.value(), loyalty.getMilesBalance().value(),
                loyalty.getLifetimeMiles().value(), loyalty.getTier());
        if (upgraded) {
            log.info("Frequent-flyer tier upgraded: userId={}, from={}, to={}",
                    idForLog(), before, loyalty.getTier());
        }
    }

    public void redeemMiles(FrequentFlyerMiles miles) {
        ensureNotDeleted("redeemMiles");
        loyalty.redeem(miles);
        log.info("Miles redeemed: userId={}, amount={}, balance={}",
                idForLog(), miles.value(), loyalty.getMilesBalance().value());
    }

    // ----- authorisation -----

    public void grantRole(Role role) {
        ensureNotDeleted("grantRole");
        if (roles.add(Objects.requireNonNull(role, "role is required"))) {
            log.info("Role granted: userId={}, role={}", idForLog(), role);
        }
    }

    public void revokeRole(Role role) {
        ensureNotDeleted("revokeRole");
        if (roles.remove(role)) {
            log.info("Role revoked: userId={}, role={}", idForLog(), role);
        }
    }

    // ----- GDPR erasure (right to be forgotten) -----

    /**
     * Erases all personally identifiable information and moves the account to the terminal
     * {@code DELETED} state. The row is retained (anonymised) so that historical references from
     * other services — booking, payment — remain referentially intact, but it can never again
     * authenticate or be mutated.
     */
    public void requestErasure(LocalDateTime at) {
        if (status == UserStatus.DELETED) {
            throw new AccountDeletedException("requestErasure");
        }
        passengerProfile.anonymize();
        loyalty.reset();
        this.email = anonymizedEmail();
        this.passwordHash = PasswordHash.of("DELETED");
        this.status = UserStatus.DELETED;
        this.deletedAt = at;
        this.lastLoginAt = null;
        this.failedLoginAttempts = 0;
        log.info("Account erased (GDPR right to be forgotten): userId={}, at={}", idForLog(), at);
    }

    private EmailAddress anonymizedEmail() {
        String token = (id != null) ? String.valueOf(id.value()) : "unknown";
        return EmailAddress.of("deleted-" + token + "@anonymized.example");
    }

    // ----- queries -----

    /** International travel requires an active account with a verified, non-expired passport. */
    public boolean canTravelInternationally(LocalDate asOf) {
        return status.isActive()
                && passengerProfile.isKycVerified()
                && passengerProfile.hasValidPassport(asOf);
    }

    public boolean isActive() {
        return status.isActive();
    }

    public boolean isDeleted() {
        return status.isDeleted();
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    // ----- accessors (read-only views; no public setters by design) -----

    public UserId getId() {
        return id;
    }

    public EmailAddress getEmail() {
        return email;
    }

    public PasswordHash getPasswordHash() {
        return passwordHash;
    }

    public UserStatus getStatus() {
        return status;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public PassengerProfile getPassengerProfile() {
        return passengerProfile;
    }

    public LoyaltyMembership getLoyalty() {
        return loyalty;
    }

    private void ensureNotDeleted(String operation) {
        if (status == UserStatus.DELETED) {
            throw new AccountDeletedException(operation);
        }
    }

    private String idForLog() {
        return id == null ? "<new>" : id.toString();
    }
}
