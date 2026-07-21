package com.abs.booking.api;

import com.abs.booking.infrastructure.client.FlightSearchHealthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class PingController {

    private final FlightSearchHealthClient flightSearchHealthClient;

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
            Map<String, Object> resp = flightSearchHealthClient.health();
            return Map.of("status", "ok", "target", "flight-search-service", "resp", resp);
        } catch (Exception e) {
            return Map.of("status", "error",
                    "target", "flight-search-service",
                    "cause", String.valueOf(e.getCause()),
                    "message", e.getMessage()
            );
        }
    }
}
