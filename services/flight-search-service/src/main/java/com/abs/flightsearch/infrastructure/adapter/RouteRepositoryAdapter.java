package com.abs.flightsearch.infrastructure.adapter;

import com.abs.flightsearch.domain.aggregate.RouteAggregate;
import com.abs.flightsearch.domain.repository.RouteRepository;
import com.abs.flightsearch.infrastructure.persistence.AirportJpaRepository;
import com.abs.flightsearch.infrastructure.persistence.RouteJpaRepository;
import com.abs.flightsearch.infrastructure.persistence.entity.RouteEntity;
import com.abs.flightsearch.infrastructure.persistence.mapper.RoutePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RouteRepositoryAdapter implements RouteRepository {
    private final RouteJpaRepository repository;
    private final AirportJpaRepository airportRepository;

    @Override
    public RouteAggregate save(RouteAggregate aggregate) {
        RouteEntity entity = RoutePersistenceMapper.toEntity(aggregate);
        entity.setFromAirport(airportRepository.getReferenceById(
                aggregate.getFromAirport().getIataCode()));
        entity.setToAirport(airportRepository.getReferenceById(
                aggregate.getToAirport().getIataCode()));
        return RoutePersistenceMapper.toAggregate(repository.save(entity));
    }

    @Override
    public Optional<RouteAggregate> findById(Long id) {
        return repository.findById(id).map(RoutePersistenceMapper::toAggregate);
    }

    @Override
    public Optional<RouteAggregate> findByAirports(String from, String to) {
        return repository.findByFromAirport_IataCodeAndToAirport_IataCode(from, to)
                .map(RoutePersistenceMapper::toAggregate);
    }

    @Override
    public List<RouteAggregate> findAll() {
        return repository.findAll().stream()
                .map(RoutePersistenceMapper::toAggregate)
                .toList();
    }
}
