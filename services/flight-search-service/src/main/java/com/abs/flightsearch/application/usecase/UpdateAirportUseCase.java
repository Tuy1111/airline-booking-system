package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.application.dto.AirportRequest;
import com.abs.flightsearch.domain.aggregate.AirportAggregate;
import com.abs.flightsearch.domain.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateAirportUseCase {

    private final AirportRepository airportRepository;

    @Transactional
    public AirportAggregate execute(String code, AirportRequest req) {
        if (!airportRepository.existsById(code)) {
            throw new IllegalArgumentException("Sân bay không tồn tại: " + code);
        }
        AirportAggregate airport = AirportAggregate.create(code, req.name(), req.city(), req.country());
        return airportRepository.save(airport);
    }
}
