package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.domain.aggregate.AirportAggregate;
import com.abs.flightsearch.domain.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAirportsUseCase {

    private final AirportRepository airportRepository;

    @Transactional(readOnly = true)
    public List<AirportAggregate> execute() {
        return airportRepository.findAll();
    }
}
