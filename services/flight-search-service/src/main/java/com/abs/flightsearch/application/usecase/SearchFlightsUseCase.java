package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.application.dto.FlightSearchResponse;
import com.abs.flightsearch.domain.aggregate.FlightAggregate;
import com.abs.flightsearch.domain.aggregate.SeatInventoryAggregate;
import com.abs.flightsearch.domain.exception.AirportNotFoundException;
import com.abs.flightsearch.domain.repository.AirportRepository;
import com.abs.flightsearch.domain.repository.FlightRepository;
import com.abs.flightsearch.domain.repository.SeatInventoryRepository;
import com.abs.flightsearch.domain.vo.FlightStatus;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchFlightsUseCase {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final SeatInventoryRepository seatInventoryRepository;
    private final MeterRegistry meterRegistry;

    @Transactional(readOnly = true)
    public List<FlightSearchResponse> execute(String from, String to, LocalDate date, int passengers) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày tìm kiếm không thể ở quá khứ");
        }
        if (!airportRepository.existsById(from)) {
            throw new AirportNotFoundException(from);
        }
        if (!airportRepository.existsById(to)) {
            throw new AirportNotFoundException(to);
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<FlightAggregate> flights = flightRepository.searchFlights(from, to, start, end, FlightStatus.SCHEDULED);

        Counter.builder("flight.search")
                .description("Number of flight searches")
                .register(meterRegistry)
                .increment();

        record FlightAndInventory(FlightAggregate flight, SeatInventoryAggregate inv) {}

        return flights.stream()
                .map(flight -> new FlightAndInventory(flight, seatInventoryRepository.findById(flight.getId()).orElse(null)))
                .filter(pair -> pair.inv() != null ? pair.inv().hasAvailableSeats(passengers) : pair.flight().getTotalSeats() >= passengers)
                .map(pair -> FlightSearchResponse.of(pair.flight(), pair.inv()))
                .toList();
    }
}
