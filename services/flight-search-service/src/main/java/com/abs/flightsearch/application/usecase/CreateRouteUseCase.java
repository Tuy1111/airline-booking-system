package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.application.dto.RouteRequest;
import com.abs.flightsearch.application.dto.RouteResponse;
import com.abs.flightsearch.domain.aggregate.AirportAggregate;
import com.abs.flightsearch.domain.aggregate.RouteAggregate;
import com.abs.flightsearch.domain.repository.AirportRepository;
import com.abs.flightsearch.domain.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateRouteUseCase {

    private final RouteRepository routeRepository;
    private final AirportRepository airportRepository;

    @Transactional
    @CacheEvict(cacheNames = "routes", allEntries = true)
    public RouteResponse execute(RouteRequest req) {
        routeRepository.findByAirports(req.fromAirport(), req.toAirport()).ifPresent(existing -> {
            throw new IllegalStateException("Tuyến bay đã tồn tại: " + req.fromAirport() + " → " + req.toAirport());
        });

        AirportAggregate from = airportRepository.findById(req.fromAirport())
                .orElseThrow(() -> new IllegalArgumentException("Sân bay đi không tồn tại: " + req.fromAirport()));
        AirportAggregate to = airportRepository.findById(req.toAirport())
                .orElseThrow(() -> new IllegalArgumentException("Sân bay đến không tồn tại: " + req.toAirport()));

        RouteAggregate route = RouteAggregate.create(from, to, req.distanceKm());
        route = routeRepository.save(route);
        return RouteResponse.of(route);
    }
}
