package com.abs.user.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;

@FeignClient(name = "user-service", contextId = "kycFeignClient", path = "/api/v1/mock/kyc")
public interface KycFeignClient {

    @PostMapping("/verify")
    KycResponse verify(@RequestBody KycRequest request);

    record KycRequest(String passportNumber, String issuingCountry, LocalDate expiryDate) {
    }

    record KycResponse(String status, String reason) {
    }
}
