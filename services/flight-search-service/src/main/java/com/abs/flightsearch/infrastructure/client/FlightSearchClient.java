package com.abs.flightsearch.infrastructure.client;

import com.abs.flightsearch.application.dto.FlightDetailResponse;
import com.abs.flightsearch.application.dto.SeatInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "flight-search-service", path = "/api/v1")
public interface FlightSearchClient {

    @GetMapping("/flights/{flightId}/seats/{seatNo}")
    SeatInfoResponse checkSeat(@PathVariable("flightId") Long flightId, @PathVariable("seatNo") String seatNo);

    @GetMapping("/flights/{id}")
    FlightDetailResponse getFlightDetail(@PathVariable("id") Long id);

    @PutMapping("/flights/{flightId}/seats/{seatNo}/hold")
    SeatInfoResponse holdSeat(@PathVariable("flightId") Long flightId, @PathVariable("seatNo") String seatNo);

    @PutMapping("/flights/{flightId}/seats/{seatNo}/book")
    SeatInfoResponse bookSeat(@PathVariable("flightId") Long flightId, @PathVariable("seatNo") String seatNo);

    @PutMapping("/flights/{flightId}/seats/{seatNo}/release")
    SeatInfoResponse releaseSeat(@PathVariable("flightId") Long flightId, @PathVariable("seatNo") String seatNo);
}
