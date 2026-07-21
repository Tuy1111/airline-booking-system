package com.abs.user.infrastructure.adapter;

import com.abs.user.application.port.out.KycVerificationPort;
import com.abs.user.domain.vo.Passport;
import com.abs.user.infrastructure.client.KycFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KycVerificationAdapter implements KycVerificationPort {

    private final KycFeignClient client;

    @Override
    public VerificationResult verify(Passport passport) {
        log.info("Calling KYC provider: issuingCountry={}", passport.issuingCountry().value());
        KycFeignClient.KycResponse response = client.verify(new KycFeignClient.KycRequest(
                passport.number().value(),
                passport.issuingCountry().value(),
                passport.expiryDate()));
        if (response == null || response.status() == null) {
            throw new IllegalStateException("KYC provider returned an empty decision");
        }

        try {
            Decision decision = Decision.valueOf(response.status().trim().toUpperCase());
            return new VerificationResult(decision, response.reason());
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("KYC provider returned an unknown decision: " + response.status(), ex);
        }
    }
}
