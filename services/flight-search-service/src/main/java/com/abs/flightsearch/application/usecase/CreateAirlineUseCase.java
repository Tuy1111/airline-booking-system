package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.application.dto.AirlineRequest;
import com.abs.flightsearch.domain.aggregate.AirlineAggregate;
import com.abs.flightsearch.domain.repository.AirlineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateAirlineUseCase {

    private final AirlineRepository airlineRepository;

    @Transactional
    public AirlineAggregate execute(AirlineRequest req) {
        airlineRepository.findById(req.code()).ifPresent(existing -> {
            throw new IllegalStateException("Hãng bay đã tồn tại: " + req.code());
        });
        AirlineAggregate airline = AirlineAggregate.create(req.code(), req.name());
        return airlineRepository.save(airline);
    }
}
