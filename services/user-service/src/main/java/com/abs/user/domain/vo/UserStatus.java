package com.abs.user.domain.vo;

/**
 * Lifecycle state of a user account, enriched with the behaviour that depends on it so callers ask
 * the status what is allowed rather than branching on the enum constant themselves.
 *
 * <ul>
 *   <li>{@code ACTIVE}  — normal, may authenticate and be mutated.</li>
 *   <li>{@code LOCKED}  — temporarily blocked (e.g. too many failed logins); cannot authenticate.</li>
 *   <li>{@code DELETED} — terminal GDPR-erased state; cannot authenticate or be mutated.</li>
 * </ul>
 */
public enum UserStatus {

    ACTIVE,
    LOCKED,
    DELETED;

    public boolean canAuthenticate() {
        return this == ACTIVE;
    }

    public boolean isDeleted() {
        return this == DELETED;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }
}
