package com.abs.flightsearch.infrastructure.client;

import com.abs.flightsearch.application.dto.FlightDetailResponse;
import com.abs.flightsearch.application.dto.SeatInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flight-search-service", path = "/api/v1")
public interface FlightSearchClient {

    @GetMapping("/flights/{flightId}/seats/{seatNo}")
    SeatInfoResponse checkSeat(@PathVariable("flightId") Long flightId, @PathVariable("seatNo") String seatNo);

    @GetMapping("/flights/{id}")
    FlightDetailResponse getFlightDetail(@PathVariable("id") Long id);
}
