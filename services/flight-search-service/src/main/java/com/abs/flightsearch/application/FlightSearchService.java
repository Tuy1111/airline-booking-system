//package com.abs.flightsearch.application;
//
//import com.abs.flightsearch.application.dto.*;
//import com.abs.flightsearch.domain.*;
//import com.abs.flightsearch.infrastructure.persistence.*;
//import io.micrometer.core.instrument.Counter;
//import io.micrometer.core.instrument.MeterRegistry;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class FlightSearchService {
//
//    private final FlightRepository flightRepository;
//    private final FlightSeatRepository flightSeatRepository;
//    private final SeatInventoryRepository seatInventoryRepository;
//    private final AirportRepository airportRepository;
//    private final AirlineRepository airlineRepository;
//    private final MeterRegistry meterRegistry;
//
//    @Transactional(readOnly = true)
//    public List<FlightSearchResponse> searchFlights(String from, String to, LocalDate date, int passengers) {
//        if (!airportRepository.existsById(from)) {
//            throw new IllegalArgumentException("Airport not found: " + from);
//        }
//        if (!airportRepository.existsById(to)) {
//            throw new IllegalArgumentException("Airport not found: " + to);
//        }
//
//        LocalDateTime start = date.atStartOfDay();
//        LocalDateTime end = date.atTime(23, 59, 59);
//
//        List<Flight> flights = flightRepository.searchFlights(from, to, start, end, FlightStatus.SCHEDULED);
//
//        Counter.builder("flight.search")
//                .description("Number of flight searches")
//                .register(meterRegistry)
//                .increment();
//
//        return flights.stream()
//                .map(flight -> {
//                    SeatInventory inv = seatInventoryRepository.findById(flight.getId()).orElse(null);
//                    return FlightSearchResponse.of(flight, inv);
//                })
//                .filter(res -> res.availableSeats() >= passengers)
//                .toList();
//    }
//
//    @Transactional(readOnly = true)
//    public FlightDetailResponse getFlightDetail(Long id) {
//        Flight flight = flightRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));
//        SeatInventory inv = seatInventoryRepository.findById(id).orElse(null);
//        return FlightDetailResponse.of(flight, inv);
//    }
//
//    @Transactional(readOnly = true)
//    public List<SeatMapResponse> getSeatMap(Long flightId) {
//        if (!flightRepository.existsById(flightId)) {
//            throw new RuntimeException("Flight not found with id: " + flightId);
//        }
//        List<FlightSeat> seats = flightSeatRepository.findByFlightId(flightId);
//        return seats.stream()
//                .map(SeatMapResponse::of)
//                .toList();
//    }
//
//    @Transactional(readOnly = true)
//    public SeatInfoResponse checkSeat(Long flightId, String seatNo) {
//        Flight flight = flightRepository.findById(flightId)
//                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + flightId));
//
//        FlightSeatId seatId = new FlightSeatId(flightId, seatNo);
//        FlightSeat seat = flightSeatRepository.findById(seatId)
//                .orElseThrow(() -> new RuntimeException("Seat " + seatNo + " not found on flight " + flightId));
//
//        SeatInventory inv = seatInventoryRepository.findById(flightId).orElse(null);
//
//        return SeatInfoResponse.of(seat, flight, inv);
//    }
//
//    @Transactional(readOnly = true)
//    public List<Airport> getAllAirports() {
//        return airportRepository.findAll();
//    }
//
//    @Transactional(readOnly = true)
//    public List<Airline> getAllAirlines() {
//        return airlineRepository.findAll();
//    }
//}
