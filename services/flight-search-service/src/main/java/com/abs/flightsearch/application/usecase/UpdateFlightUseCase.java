package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.application.dto.FlightDetailResponse;
import com.abs.flightsearch.application.dto.FlightUpdateRequest;
import com.abs.flightsearch.domain.aggregate.FlightAggregate;
import com.abs.flightsearch.domain.aggregate.SeatInventoryAggregate;
import com.abs.flightsearch.domain.exception.FlightNotFoundException;
import com.abs.flightsearch.domain.repository.FlightRepository;
import com.abs.flightsearch.domain.repository.SeatInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateFlightUseCase {

    private final FlightRepository flightRepository;
    private final SeatInventoryRepository seatInventoryRepository;

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = {"flightSearch", "upcomingFlights", "adminFlights"}, allEntries = true),
            @CacheEvict(cacheNames = "flightDetail", key = "#id")
    })
    public FlightDetailResponse execute(Long id, FlightUpdateRequest req) {
        FlightAggregate flight = flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException(id));

        flight.updateDetails(req.departureTime(), req.arrivalTime(), req.basePrice(), req.aircraftType());
        flight = flightRepository.save(flight);

        SeatInventoryAggregate inv = seatInventoryRepository.findById(id).orElse(null);
        if (inv != null && req.totalSeats() != null) {
            inv.updateTotalSeats(req.totalSeats());
            seatInventoryRepository.save(inv);
        }

        return FlightDetailResponse.of(flight, inv);
    }
}
