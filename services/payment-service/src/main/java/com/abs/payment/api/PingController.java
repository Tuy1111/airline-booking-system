package com.abs.payment.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PingController {

    private final RestTemplate restTemplate;

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of("status", "ok",
                "service", "payment-service",
                "message", "Payment Service Hello World!"
        );
    }
}
