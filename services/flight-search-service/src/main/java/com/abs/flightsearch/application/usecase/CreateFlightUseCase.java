package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.application.dto.FlightCreateRequest;
import com.abs.flightsearch.application.dto.FlightDetailResponse;
import com.abs.flightsearch.domain.aggregate.*;
import com.abs.flightsearch.domain.repository.*;
import com.abs.flightsearch.domain.vo.SeatClass;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreateFlightUseCase {

    private final FlightRepository flightRepository;
    private final RouteRepository routeRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;
    private final SeatInventoryRepository seatInventoryRepository;
    private final FlightSeatRepository flightSeatRepository;

    @Transactional
    public FlightDetailResponse execute(FlightCreateRequest req) {
        AirportAggregate fromAirport = airportRepository.findById(req.fromAirportCode())
                .orElseThrow(() -> new IllegalArgumentException("Sân bay đi không tồn tại: " + req.fromAirportCode()));
        AirportAggregate toAirport = airportRepository.findById(req.toAirportCode())
                .orElseThrow(() -> new IllegalArgumentException("Sân bay đến không tồn tại: " + req.toAirportCode()));
        AirlineAggregate airline = airlineRepository.findById(req.airlineCode())
                .orElseThrow(() -> new IllegalArgumentException("Hãng bay không tồn tại: " + req.airlineCode()));

        RouteAggregate route = routeRepository.findByAirports(req.fromAirportCode(), req.toAirportCode())
                .orElseThrow(() -> new IllegalArgumentException("Tuyến bay không tồn tại: " + req.fromAirportCode() + " → " + req.toAirportCode()));

        FlightAggregate flight = FlightAggregate.create(
                req.flightNo(), route, airline,
                req.departureTime(), req.arrivalTime(),
                req.totalSeats(), req.basePrice(), req.aircraftType());
        flight = flightRepository.save(flight);

        SeatInventoryAggregate inv = SeatInventoryAggregate.create(flight.getId(), req.totalSeats());
        seatInventoryRepository.save(inv);

        int rows = req.totalSeats() / 6;
        String[] letters = {"A", "B", "C", "D", "E", "F"};
        for (int r = 1; r <= rows; r++) {
            SeatClass sClass = (r <= 3) ? SeatClass.BUSINESS : SeatClass.ECONOMY;
            BigDecimal factor = (r <= 3) ? BigDecimal.valueOf(1.5) : BigDecimal.ONE;
            for (String letter : letters) {
                FlightSeatAggregate seat = FlightSeatAggregate.create(flight.getId(), r + letter, sClass, factor);
                flightSeatRepository.save(seat);
            }
        }

        return FlightDetailResponse.of(flight, inv);
    }
}
