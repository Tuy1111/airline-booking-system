package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.application.dto.FlightSearchResponse;
import com.abs.flightsearch.domain.repository.FlightRepository;
import com.abs.flightsearch.domain.repository.SeatInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllFlightsUseCase {

    private final FlightRepository flightRepository;
    private final SeatInventoryRepository seatInventoryRepository;

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "adminFlights")
    public List<FlightSearchResponse> execute() {
        return flightRepository.findAll().stream()
                .map(flight -> FlightSearchResponse.of(
                        flight,
                        seatInventoryRepository.findById(flight.getId()).orElse(null)))
                .toList();
    }
}
