package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.application.dto.AirportRequest;
import com.abs.flightsearch.domain.aggregate.AirportAggregate;
import com.abs.flightsearch.domain.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateAirportUseCase {

    private final AirportRepository airportRepository;

    @Transactional
    @CacheEvict(cacheNames = "airports", allEntries = true)
    public AirportAggregate execute(AirportRequest req) {
        if (airportRepository.existsById(req.iataCode())) {
            throw new IllegalStateException("Sân bay đã tồn tại: " + req.iataCode());
        }
        AirportAggregate airport = AirportAggregate.create(req.iataCode(), req.name(), req.city(), req.country());
        return airportRepository.save(airport);
    }
}
