package com.abs.flightsearch.infrastructure.adapter;

import com.abs.flightsearch.domain.aggregate.FlightAggregate;
import com.abs.flightsearch.domain.repository.FlightRepository;
import com.abs.flightsearch.domain.vo.FlightStatus;
import com.abs.flightsearch.infrastructure.persistence.AirlineJpaRepository;
import com.abs.flightsearch.infrastructure.persistence.FlightJpaRepository;
import com.abs.flightsearch.infrastructure.persistence.RouteJpaRepository;
import com.abs.flightsearch.infrastructure.persistence.entity.FlightEntity;
import com.abs.flightsearch.infrastructure.persistence.mapper.FlightPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class FlightRepositoryAdapter implements FlightRepository {
    private final FlightJpaRepository repository;
    private final RouteJpaRepository routeRepository;
    private final AirlineJpaRepository airlineRepository;

    @Override
    public FlightAggregate save(FlightAggregate aggregate) {
        FlightEntity entity = FlightPersistenceMapper.toEntity(aggregate);
        entity.setRoute(routeRepository.getReferenceById(aggregate.getRoute().getId()));
        entity.setAirline(airlineRepository.getReferenceById(aggregate.getAirline().getCode()));
        return FlightPersistenceMapper.toAggregate(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FlightAggregate> findById(Long id) {
        return repository.findById(id).map(FlightPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FlightAggregate> findByFlightNo(String flightNo) {
        return repository.findByFlightNo(flightNo).map(FlightPersistenceMapper::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightAggregate> searchFlights(
            String from,
            String to,
            LocalDateTime start,
            LocalDateTime end,
            FlightStatus status) {
        return repository.searchFlights(from, to, start, end, status).stream()
                .map(FlightPersistenceMapper::toAggregate)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightAggregate> findUpcoming(LocalDateTime from, LocalDateTime to) {
        return repository.findUpcoming(from, to).stream()
                .map(FlightPersistenceMapper::toAggregate)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightAggregate> findByStatus(FlightStatus status) {
        return repository.findByStatus(status).stream()
                .map(FlightPersistenceMapper::toAggregate)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightAggregate> findAll() {
        return repository.findAll().stream()
                .map(FlightPersistenceMapper::toAggregate)
                .toList();
    }
}
