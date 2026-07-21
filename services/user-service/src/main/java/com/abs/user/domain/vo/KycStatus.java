package com.abs.user.domain.vo;

/**
 * Know-Your-Customer verification state for a passenger's identity document.
 *
 * <ul>
 *   <li>{@code UNVERIFIED} — no document on file, or none submitted for review.</li>
 *   <li>{@code PENDING}    — a passport has been submitted and is awaiting verification.</li>
 *   <li>{@code VERIFIED}   — the document was checked and accepted.</li>
 *   <li>{@code REJECTED}   — the document was checked and refused (e.g. expired, mismatch).</li>
 * </ul>
 */
public enum KycStatus {

    UNVERIFIED,
    PENDING,
    VERIFIED,
    REJECTED;

    public boolean isVerified() {
        return this == VERIFIED;
    }
}
