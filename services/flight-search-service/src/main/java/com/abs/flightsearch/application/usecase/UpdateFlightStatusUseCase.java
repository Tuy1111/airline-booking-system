package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.application.dto.FlightDetailResponse;
import com.abs.flightsearch.domain.aggregate.FlightAggregate;
import com.abs.flightsearch.domain.aggregate.SeatInventoryAggregate;
import com.abs.flightsearch.domain.exception.FlightNotFoundException;
import com.abs.flightsearch.domain.repository.FlightRepository;
import com.abs.flightsearch.domain.repository.SeatInventoryRepository;
import com.abs.flightsearch.domain.vo.FlightStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateFlightStatusUseCase {

    private final FlightRepository flightRepository;
    private final SeatInventoryRepository seatInventoryRepository;

    @Transactional
    public FlightDetailResponse execute(Long id, FlightStatus newStatus, LocalDateTime newDeparture, LocalDateTime newArrival) {
        FlightAggregate flight = flightRepository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException(id));

        switch (newStatus) {
            case DELAYED -> {
                if (newDeparture == null || newArrival == null) {
                    throw new IllegalArgumentException("Cần cung cấp thời gian mới khi delay chuyến bay");
                }
                flight.delay(newDeparture, newArrival);
            }
            case CANCELLED -> flight.cancel();
            case DEPARTED -> flight.depart();
            case SCHEDULED -> flight.schedule();
        }

        flight = flightRepository.save(flight);
        SeatInventoryAggregate inv = seatInventoryRepository.findById(id).orElse(null);
        return FlightDetailResponse.of(flight, inv);
    }
}
