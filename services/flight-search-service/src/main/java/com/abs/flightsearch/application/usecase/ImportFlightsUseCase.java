package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.application.dto.FlightImportRequest;
import com.abs.flightsearch.domain.aggregate.*;
import com.abs.flightsearch.domain.repository.*;
import com.abs.flightsearch.domain.vo.SeatClass;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImportFlightsUseCase {

    private final AirportRepository airportRepository;
    private final AirlineRepository airlineRepository;
    private final RouteRepository routeRepository;
    private final FlightRepository flightRepository;
    private final SeatInventoryRepository seatInventoryRepository;
    private final FlightSeatRepository flightSeatRepository;

    @Transactional
    @CacheEvict(cacheNames = {"airports", "airlines", "routes", "flightSearch", "flightDetail", "upcomingFlights", "adminFlights"}, allEntries = true)
    public void execute(List<FlightImportRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return;
        }

        for (FlightImportRequest req : requests) {
            AirportAggregate fromAirport = airportRepository.findById(req.fromAirportCode())
                    .orElseGet(() -> airportRepository.save(
                            AirportAggregate.create(req.fromAirportCode(), req.fromAirportName(), req.fromAirportCity(), req.fromAirportCountry())
                    ));

            AirportAggregate toAirport = airportRepository.findById(req.toAirportCode())
                    .orElseGet(() -> airportRepository.save(
                            AirportAggregate.create(req.toAirportCode(), req.toAirportName(), req.toAirportCity(), req.toAirportCountry())
                    ));

            AirlineAggregate airline = airlineRepository.findById(req.airlineCode())
                    .orElseGet(() -> airlineRepository.save(
                            AirlineAggregate.create(req.airlineCode(), req.airlineName())
                    ));

            RouteAggregate route = routeRepository.findByAirports(fromAirport.getIataCode(), toAirport.getIataCode())
                    .orElseGet(() -> routeRepository.save(
                            RouteAggregate.create(fromAirport, toAirport, req.distanceKm())
                    ));

            Optional<FlightAggregate> existingFlightOpt = flightRepository.findByFlightNo(req.flightNo());
            if (existingFlightOpt.isPresent()) {
                FlightAggregate flight = existingFlightOpt.get();
                flight.updateDetails(req.departureTime(), req.arrivalTime(), req.basePrice(), req.aircraftType());
                flightRepository.save(flight);

                SeatInventoryAggregate inv = seatInventoryRepository.findById(flight.getId())
                        .orElseGet(() -> SeatInventoryAggregate.create(flight.getId(), req.totalSeats()));
                inv.updateTotalSeats(req.totalSeats());
                seatInventoryRepository.save(inv);
            } else {
                FlightAggregate flight = FlightAggregate.create(
                        req.flightNo(), route, airline, req.departureTime(), req.arrivalTime(), req.totalSeats(), req.basePrice(), req.aircraftType()
                );
                flight = flightRepository.save(flight);

                SeatInventoryAggregate inv = SeatInventoryAggregate.create(flight.getId(), req.totalSeats());
                seatInventoryRepository.save(inv);

                int rows = req.totalSeats() / 6;
                for (int r = 1; r <= rows; r++) {
                    SeatClass sClass = (r <= 3) ? SeatClass.BUSINESS : SeatClass.ECONOMY;
                    BigDecimal factor = (r <= 3) ? BigDecimal.valueOf(1.5) : BigDecimal.ONE;
                    String[] letters = {"A", "B", "C", "D", "E", "F"};
                    for (String letter : letters) {
                        String seatNo = r + letter;
                        FlightSeatAggregate seat = FlightSeatAggregate.create(flight.getId(), seatNo, sClass, factor);
                        flightSeatRepository.save(seat);
                    }
                }
            }
        }
    }
}
