package com.abs.flightsearch.application;

import com.abs.flightsearch.application.dto.*;
import com.abs.flightsearch.domain.aggregate.AirlineAggregate;
import com.abs.flightsearch.domain.aggregate.AirportAggregate;
import com.abs.flightsearch.domain.aggregate.FlightAggregate;
import com.abs.flightsearch.domain.aggregate.FlightSeatAggregate;
import com.abs.flightsearch.domain.aggregate.SeatInventoryAggregate;
import com.abs.flightsearch.domain.vo.FlightSeatId;
import com.abs.flightsearch.domain.vo.FlightStatus;
import com.abs.flightsearch.domain.repository.AirlineRepository;
import com.abs.flightsearch.domain.repository.AirportRepository;
import com.abs.flightsearch.domain.repository.FlightRepository;
import com.abs.flightsearch.domain.repository.FlightSeatRepository;
import com.abs.flightsearch.domain.repository.SeatInventoryRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightSearchService {

    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final SeatInventoryRepository seatInventoryRepository;
    private final AirportRepository airportRepository;
    private final AirlineRepository airlineRepository;
    private final MeterRegistry meterRegistry;

    @Transactional(readOnly = true)
    public List<FlightSearchResponse> searchFlights(String from, String to, LocalDate date, int passengers) {
        if (!airportRepository.existsById(from)) {
            throw new IllegalArgumentException("Airport not found: " + from);
        }
        if (!airportRepository.existsById(to)) {
            throw new IllegalArgumentException("Airport not found: " + to);
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<FlightAggregate> flights = flightRepository.searchFlights(from, to, start, end, FlightStatus.SCHEDULED);

        Counter.builder("flight.search")
                .description("Number of flight searches")
                .register(meterRegistry)
                .increment();

        return flights.stream()
                .map(flight -> {
                    SeatInventoryAggregate inv = seatInventoryRepository.findById(flight.getId()).orElse(null);
                    return FlightSearchResponse.of(flight, inv);
                })
                .filter(res -> res.availableSeats() >= passengers)
                .toList();
    }

    @Transactional(readOnly = true)
    public FlightDetailResponse getFlightDetail(Long id) {
        FlightAggregate flight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));
        SeatInventoryAggregate inv = seatInventoryRepository.findById(id).orElse(null);
        return FlightDetailResponse.of(flight, inv);
    }

    @Transactional(readOnly = true)
    public List<SeatMapResponse> getSeatMap(Long flightId) {
        if (!flightRepository.existsById(flightId)) {
            throw new RuntimeException("Flight not found with id: " + flightId);
        }
        List<FlightSeatAggregate> seats = flightSeatRepository.findByFlightId(flightId);
        return seats.stream()
                .map(SeatMapResponse::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public SeatInfoResponse checkSeat(Long flightId, String seatNo) {
        FlightAggregate flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + flightId));

        FlightSeatId seatId = new FlightSeatId(flightId, seatNo);
        FlightSeatAggregate seat = flightSeatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat " + seatNo + " not found on flight " + flightId));

        SeatInventoryAggregate inv = seatInventoryRepository.findById(flightId).orElse(null);

        return SeatInfoResponse.of(seat, flight, inv);
    }

    @Transactional(readOnly = true)
    public List<AirportAggregate> getAllAirports() {
        return airportRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<AirlineAggregate> getAllAirlines() {
        return airlineRepository.findAll();
    }
}
