package com.abs.user.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Locale;

/** Deterministic development stub standing in for an external passport authority. */
@RestController
@RequestMapping("/mock/kyc")
@Slf4j
public class MockKycThirdPartyController {

    @PostMapping("/verify")
    public KycResponse verify(@RequestBody KycRequest request) {
        KycResponse response = evaluate(request);
        log.info("Mock KYC decision: issuingCountry={}, status={}, reason={}",
                request.issuingCountry(), response.status(), response.reason());
        return response;
    }

    private KycResponse evaluate(KycRequest request) {
        if (request.passportNumber() == null || request.passportNumber().isBlank()) {
            return rejected("Passport number is missing");
        }
        if (request.issuingCountry() == null || !request.issuingCountry().matches("[A-Za-z]{3}")) {
            return rejected("Issuing country must be a 3-letter code");
        }
        if (request.expiryDate() == null || !request.expiryDate().isAfter(LocalDate.now())) {
            return rejected("Passport is expired");
        }
        if (request.passportNumber().trim().toUpperCase(Locale.ROOT).startsWith("REJECT")) {
            return rejected("Passport was flagged by the mock authority");
        }
        return new KycResponse("VERIFIED", "Passport matched the mock authority records");
    }

    private KycResponse rejected(String reason) {
        return new KycResponse("REJECTED", reason);
    }

    public record KycRequest(String passportNumber, String issuingCountry, LocalDate expiryDate) {
    }

    public record KycResponse(String status, String reason) {
    }
}
