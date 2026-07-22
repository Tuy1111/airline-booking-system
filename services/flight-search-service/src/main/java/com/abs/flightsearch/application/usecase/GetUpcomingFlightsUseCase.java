package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.application.dto.FlightSearchResponse;
import com.abs.flightsearch.domain.aggregate.FlightAggregate;
import com.abs.flightsearch.domain.aggregate.SeatInventoryAggregate;
import com.abs.flightsearch.domain.repository.FlightRepository;
import com.abs.flightsearch.domain.repository.SeatInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUpcomingFlightsUseCase {

    private final FlightRepository flightRepository;
    private final SeatInventoryRepository seatInventoryRepository;

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "upcomingFlights", key = "#hours")
    public List<FlightSearchResponse> execute(int hours) {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plusHours(hours);

        List<FlightAggregate> flights = flightRepository.findUpcoming(from, to);

        return flights.stream()
                .map(flight -> {
                    SeatInventoryAggregate inv = seatInventoryRepository.findById(flight.getId()).orElse(null);
                    return FlightSearchResponse.of(flight, inv);
                })
                .toList();
    }
}
