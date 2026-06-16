package com.abs.flightsearch.api;

import com.abs.flightsearch.application.dto.FlightDetailInfo;
import com.abs.flightsearch.application.dto.FlightResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller trả về fake data, không dùng DB.
 * Booking-service sẽ gọi qua OpenFeign tới các endpoint này.
 */
@RestController
@RequestMapping("/api/v1/flights")
public class FlightInfoController {

    /**
     * GET /api/v1/flights?from=HAN&to=SGN&date=2026-06-20
     */
    @GetMapping
    public ResponseEntity<List<FlightResponse>> searchFlights(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("date") String date) {

        List<FlightResponse> flights = List.of(
                new FlightResponse(
                        1L, "VN-101", from, to,
                        date + "T08:00:00", date + "T10:15:00",
                        new BigDecimal("1500000"), 45, "Vietnam Airlines"
                ),
                new FlightResponse(
                        2L, "VJ-202", from, to,
                        date + "T14:30:00", date + "T16:45:00",
                        new BigDecimal("900000"), 120, "VietJet Air"
                )
        );

        return ResponseEntity.ok(flights);
    }

    /**
     * GET /api/v1/flights/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<FlightDetailInfo> getFlightById(@PathVariable("id") Long id) {

        FlightDetailInfo flight = new FlightDetailInfo(
                id, "VN-101", "HAN", "SGN",
                "2026-06-20T08:00:00", "2026-06-20T10:15:00",
                new BigDecimal("1500000"), 45, "Vietnam Airlines", "SCHEDULED"
        );

        return ResponseEntity.ok(flight);
    }
    
    @GetMapping("/ping")
    public Boolean check() {
        return true;
    }
}
