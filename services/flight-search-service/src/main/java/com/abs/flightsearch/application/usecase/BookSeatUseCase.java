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

@Service
@RequiredArgsConstructor
public class BookSeatUseCase {

    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final SeatInventoryRepository seatInventoryRepository;

    @Transactional
    public SeatInfoResponse execute(Long flightId, String seatNo) {
        FlightAggregate flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));

        FlightSeatAggregate seat = flightSeatRepository.findById(new FlightSeatId(flightId, seatNo))
                .orElseThrow(() -> new IllegalArgumentException("Ghế " + seatNo + " không tồn tại trên chuyến bay " + flightId));

        seat.book();
        flightSeatRepository.save(seat);

        SeatInventoryAggregate inv = seatInventoryRepository.findById(flightId)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy thông tin ghế cho chuyến bay " + flightId));
        inv.confirmSeats(1);
        seatInventoryRepository.save(inv);

        return SeatInfoResponse.of(seat, flight, inv);
    }
}
