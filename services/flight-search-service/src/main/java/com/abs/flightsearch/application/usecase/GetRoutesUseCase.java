package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.application.dto.RouteResponse;
import com.abs.flightsearch.domain.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetRoutesUseCase {

    private final RouteRepository routeRepository;

    @Transactional(readOnly = true)
    public List<RouteResponse> execute() {
        return routeRepository.findAll().stream()
                .map(RouteResponse::of)
                .toList();
    }
}
