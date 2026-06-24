//package com.abs.flightsearch.api;
//
//import com.abs.flightsearch.application.FlightSearchService;
//import com.abs.flightsearch.application.dto.*;
//import com.abs.flightsearch.domain.Airline;
//import com.abs.flightsearch.domain.Airport;
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotBlank;
//import lombok.RequiredArgsConstructor;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@RestController
//@RequestMapping("/flights")
//@RequiredArgsConstructor
//@Validated
//public class FlightController {
//
//    private final FlightSearchService flightSearchService;
//
//    @GetMapping
//    public ResponseEntity<List<FlightSearchResponse>> searchFlights(
//            @RequestParam @NotBlank String from,
//            @RequestParam @NotBlank String to,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
//            @RequestParam(defaultValue = "1") @Min(1) int passengers) {
//        List<FlightSearchResponse> responses = flightSearchService.searchFlights(from, to, date, passengers);
//        return ResponseEntity.ok(responses);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<FlightDetailResponse> getFlightDetail(@PathVariable Long id) {
//        FlightDetailResponse response = flightSearchService.getFlightDetail(id);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/{flightId}/seats")
//    public ResponseEntity<List<SeatMapResponse>> getSeatMap(@PathVariable Long flightId) {
//        List<SeatMapResponse> response = flightSearchService.getSeatMap(flightId);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/{flightId}/seats/{seatNo}")
//    public ResponseEntity<SeatInfoResponse> checkSeat(
//            @PathVariable Long flightId,
//            @PathVariable String seatNo) {
//        SeatInfoResponse response = flightSearchService.checkSeat(flightId, seatNo);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/airports")
//    public ResponseEntity<List<Airport>> getAllAirports() {
//        List<Airport> response = flightSearchService.getAllAirports();
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/airlines")
//    public ResponseEntity<List<Airline>> getAllAirlines() {
//        List<Airline> response = flightSearchService.getAllAirlines();
//        return ResponseEntity.ok(response);
//    }
//}
