package com.abs.user.application.port.out;

import com.abs.user.domain.vo.Passport;

/** Outbound application port for checking a passport with an external KYC provider. */
public interface KycVerificationPort {

    VerificationResult verify(Passport passport);

    enum Decision {
        VERIFIED,
        REJECTED
    }

    record VerificationResult(Decision decision, String reason) {
        public VerificationResult {
            if (decision == null) {
                throw new IllegalArgumentException("KYC decision is required");
            }
        }

        public boolean isVerified() {
            return decision == Decision.VERIFIED;
        }
    }
}
