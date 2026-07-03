package com.abs.booking.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class PingController {

    private final RestTemplate restTemplate;

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of("status", "ok",
                "service", "booking-service",
                "message", "Booking Service Hello World!"
        );
    }

    @GetMapping("/ping-flight")
    public Map<String, Object> pingFlight() {
        try {
            Map<?, ?> resp = restTemplate.getForObject(
                    "http://localhost:8081/api/v1/flights/ping", Map.class);
            return Map.of("status", resp.get("status"), "resp", resp);
        } catch (Exception e) {
            return Map.of("status", "error",
                    "cause", String.valueOf(e.getCause()),
                    "message", e.getMessage()
            );
        }
    }
}
