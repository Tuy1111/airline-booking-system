package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.application.dto.SeatInfoResponse;
import com.abs.flightsearch.domain.aggregate.FlightAggregate;
import com.abs.flightsearch.domain.aggregate.FlightSeatAggregate;
import com.abs.flightsearch.domain.aggregate.SeatInventoryAggregate;
import com.abs.flightsearch.domain.exception.FlightNotFoundException;
import com.abs.flightsearch.domain.repository.FlightRepository;
import com.abs.flightsearch.domain.repository.FlightSeatRepository;
import com.abs.flightsearch.domain.repository.SeatInventoryRepository;
import com.abs.flightsearch.domain.vo.FlightSeatId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckSeatAvailabilityUseCase {

    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final SeatInventoryRepository seatInventoryRepository;

    @Transactional(readOnly = true)
    public SeatInfoResponse execute(Long flightId, String seatNo) {
        FlightAggregate flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));
        flight.ensureBookableAt(LocalDateTime.now());

        FlightSeatId seatId = new FlightSeatId(flightId, seatNo);
        FlightSeatAggregate seat = flightSeatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Seat " + seatNo + " not found on flight " + flightId));

        SeatInventoryAggregate inv = seatInventoryRepository.findById(flightId).orElse(null);

        return SeatInfoResponse.of(seat, flight, inv);
    }
}
