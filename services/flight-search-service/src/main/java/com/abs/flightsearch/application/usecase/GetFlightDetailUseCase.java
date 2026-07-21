package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.application.dto.FlightDetailResponse;
import com.abs.flightsearch.domain.aggregate.FlightAggregate;
import com.abs.flightsearch.domain.aggregate.SeatInventoryAggregate;
import com.abs.flightsearch.domain.exception.FlightNotFoundException;
import com.abs.flightsearch.domain.repository.FlightRepository;
import com.abs.flightsearch.domain.repository.SeatInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetFlightDetailUseCase {

    private final FlightRepository flightRepository;
    private final SeatInventoryRepository seatInventoryRepository;

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "flightDetail", key = "#id")
    public FlightDetailResponse execute(Long id) {
        FlightAggregate flight = flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException(id));
        SeatInventoryAggregate inv = seatInventoryRepository.findById(id).orElse(null);
        return FlightDetailResponse.of(flight, inv);
    }
}
