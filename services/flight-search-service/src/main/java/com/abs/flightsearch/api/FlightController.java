package com.abs.flightsearch.api;

import com.abs.flightsearch.application.dto.*;
import com.abs.flightsearch.application.usecase.*;
import com.abs.flightsearch.domain.aggregate.AirlineAggregate;
import com.abs.flightsearch.domain.aggregate.AirportAggregate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(ApiPath.FLIGHTS)
@RequiredArgsConstructor
@Validated
public class FlightController {

    private final SearchFlightsUseCase searchFlightsUseCase;
    private final GetFlightDetailUseCase getFlightDetailUseCase;
    private final GetSeatMapUseCase getSeatMapUseCase;
    private final CheckSeatAvailabilityUseCase checkSeatAvailabilityUseCase;
    private final GetAirportsUseCase getAirportsUseCase;
    private final GetAirlinesUseCase getAirlinesUseCase;
    private final ImportFlightsUseCase importFlightsUseCase;

    @GetMapping
    public ResponseEntity<List<FlightSearchResponse>> searchFlights(
            @RequestParam @NotBlank String from,
            @RequestParam @NotBlank String to,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "1") @Min(1) int passengers) {
        List<FlightSearchResponse> responses = searchFlightsUseCase.execute(from, to, date, passengers);
        return ResponseEntity.ok(responses);
    }

    @GetMapping(ApiPath.BY_ID)
    public ResponseEntity<FlightDetailResponse> getFlightDetail(@PathVariable Long id) {
        FlightDetailResponse response = getFlightDetailUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(ApiPath.SEATS)
    public ResponseEntity<List<SeatMapResponse>> getSeatMap(@PathVariable Long flightId) {
        List<SeatMapResponse> response = getSeatMapUseCase.execute(flightId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(ApiPath.CHECK_SEAT)
    public ResponseEntity<SeatInfoResponse> checkSeat(
            @PathVariable Long flightId,
            @PathVariable String seatNo) {
        SeatInfoResponse response = checkSeatAvailabilityUseCase.execute(flightId, seatNo);
        return ResponseEntity.ok(response);
    }

    @GetMapping(ApiPath.AIRPORTS)
    public ResponseEntity<List<AirportAggregate>> getAllAirports() {
        List<AirportAggregate> response = getAirportsUseCase.execute();
        return ResponseEntity.ok(response);
    }

    @GetMapping(ApiPath.AIRLINES)
    public ResponseEntity<List<AirlineAggregate>> getAllAirlines() {
        List<AirlineAggregate> response = getAirlinesUseCase.execute();
        return ResponseEntity.ok(response);
    }

    @PostMapping(ApiPath.IMPORT)
    public ResponseEntity<Void> importFlights(@RequestBody List<FlightImportRequest> requests) {
        importFlightsUseCase.execute(requests);
        return ResponseEntity.ok().build();
    }
}
