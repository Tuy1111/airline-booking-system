package com.abs.flightsearch.api;

import com.abs.flightsearch.application.dto.*;
import com.abs.flightsearch.application.usecase.*;
import com.abs.flightsearch.domain.aggregate.AirlineAggregate;
import com.abs.flightsearch.domain.aggregate.AirportAggregate;
import com.abs.flightsearch.domain.vo.FlightStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    // P0 Usecases
    private final HoldSeatUseCase holdSeatUseCase;
    private final BookSeatUseCase bookSeatUseCase;
    private final ReleaseSeatUseCase releaseSeatUseCase;

    // P1 Usecases
    private final UpdateFlightStatusUseCase updateFlightStatusUseCase;
    private final GetUpcomingFlightsUseCase getUpcomingFlightsUseCase;

    // P2 Usecases
    private final CreateFlightUseCase createFlightUseCase;
    private final UpdateFlightUseCase updateFlightUseCase;
    private final DeleteFlightUseCase deleteFlightUseCase;

    @GetMapping
    public ResponseEntity<List<FlightSearchResponse>> searchFlights(
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to,
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name = "passengers", defaultValue = "1") @Min(1) int passengers,
            @RequestParam(name = "status", required = false) FlightStatus status,
            @RequestParam(name = "airline", required = false) String airline,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "dateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(name = "sort", required = false, defaultValue = "departureTime") String sort,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        List<FlightSearchResponse> responses = searchFlightsUseCase.execute(
                from, to, date, passengers, status, airline, minPrice, maxPrice, dateTo, sort, order);
        return ResponseEntity.ok(responses);
    }

    @GetMapping(ApiPath.BY_ID)
    public ResponseEntity<FlightDetailResponse> getFlightDetail(@PathVariable("id") Long id) {
        FlightDetailResponse response = getFlightDetailUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(ApiPath.SEATS)
    public ResponseEntity<List<SeatMapResponse>> getSeatMap(@PathVariable("flightId") Long flightId) {
        List<SeatMapResponse> response = getSeatMapUseCase.execute(flightId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(ApiPath.CHECK_SEAT)
    public ResponseEntity<SeatInfoResponse> checkSeat(
            @PathVariable("flightId") Long flightId,
            @PathVariable("seatNo") String seatNo) {
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

    // P0 Endpoints
    @PutMapping(ApiPath.HOLD_SEAT)
    public ResponseEntity<SeatInfoResponse> holdSeat(
            @PathVariable("flightId") Long flightId,
            @PathVariable("seatNo") String seatNo) {
        SeatInfoResponse response = holdSeatUseCase.execute(flightId, seatNo);
        return ResponseEntity.ok(response);
    }

    @PutMapping(ApiPath.BOOK_SEAT)
    public ResponseEntity<SeatInfoResponse> bookSeat(
            @PathVariable("flightId") Long flightId,
            @PathVariable("seatNo") String seatNo) {
        SeatInfoResponse response = bookSeatUseCase.execute(flightId, seatNo);
        return ResponseEntity.ok(response);
    }

    @PutMapping(ApiPath.RELEASE_SEAT)
    public ResponseEntity<SeatInfoResponse> releaseSeat(
            @PathVariable("flightId") Long flightId,
            @PathVariable("seatNo") String seatNo) {
        SeatInfoResponse response = releaseSeatUseCase.execute(flightId, seatNo);
        return ResponseEntity.ok(response);
    }

    // P1 Endpoints
    @PutMapping(ApiPath.FLIGHT_STATUS)
    public ResponseEntity<FlightDetailResponse> updateFlightStatus(
            @PathVariable("id") Long id,
            @RequestBody FlightStatusUpdateRequest request) {
        FlightDetailResponse response = updateFlightStatusUseCase.execute(
                id, request.status(), request.newDepartureTime(), request.newArrivalTime());
        return ResponseEntity.ok(response);
    }

    @GetMapping(ApiPath.UPCOMING)
    public ResponseEntity<List<FlightSearchResponse>> getUpcomingFlights(
            @RequestParam(name = "hours", defaultValue = "24") int hours) {
        List<FlightSearchResponse> response = getUpcomingFlightsUseCase.execute(hours);
        return ResponseEntity.ok(response);
    }

    // P2 Endpoints
    @PostMapping
    public ResponseEntity<FlightDetailResponse> createFlight(@Valid @RequestBody FlightCreateRequest request) {
        FlightDetailResponse response = createFlightUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(ApiPath.BY_ID)
    public ResponseEntity<FlightDetailResponse> updateFlight(
            @PathVariable("id") Long id,
            @RequestBody FlightUpdateRequest request) {
        FlightDetailResponse response = updateFlightUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(ApiPath.BY_ID)
    public ResponseEntity<Void> deleteFlight(@PathVariable("id") Long id) {
        deleteFlightUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
