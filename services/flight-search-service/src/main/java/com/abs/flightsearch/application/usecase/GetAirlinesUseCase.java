package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.domain.aggregate.AirlineAggregate;
import com.abs.flightsearch.domain.repository.AirlineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAirlinesUseCase {

    private final AirlineRepository airlineRepository;

    @Transactional(readOnly = true)
    public List<AirlineAggregate> execute() {
        return airlineRepository.findAll();
    }
}
