package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.application.dto.SeatMapResponse;
import com.abs.flightsearch.domain.aggregate.FlightSeatAggregate;
import com.abs.flightsearch.domain.exception.FlightNotFoundException;
import com.abs.flightsearch.domain.repository.FlightRepository;
import com.abs.flightsearch.domain.repository.FlightSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetSeatMapUseCase {

    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;

    @Transactional(readOnly = true)
    public List<SeatMapResponse> execute(Long flightId) {
        if (!flightRepository.existsById(flightId)) {
            throw new FlightNotFoundException(flightId);
        }
        List<FlightSeatAggregate> seats = flightSeatRepository.findByFlightId(flightId);
        return seats.stream()
                .map(SeatMapResponse::of)
                .toList();
    }
}
