package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.domain.exception.FlightNotFoundException;
import com.abs.flightsearch.domain.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteFlightUseCase {

    private final FlightRepository flightRepository;

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = {"flightSearch", "upcomingFlights", "adminFlights"}, allEntries = true),
            @CacheEvict(cacheNames = "flightDetail", key = "#id")
    })
    public void execute(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new FlightNotFoundException(id);
        }
        flightRepository.deleteById(id);
    }
}
