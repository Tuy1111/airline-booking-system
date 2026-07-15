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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SearchFlightsUseCase {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final SeatInventoryRepository seatInventoryRepository;
    private final MeterRegistry meterRegistry;

    @Transactional(readOnly = true)
    public List<FlightSearchResponse> execute(String from, String to, LocalDate date, int passengers) {
        return execute(from, to, date, passengers, FlightStatus.SCHEDULED, null, null, null, null, "departureTime", "asc");
    }

    @Transactional(readOnly = true)
    public List<FlightSearchResponse> execute(
            String from, String to, LocalDate date, int passengers,
            FlightStatus status, String airline, BigDecimal minPrice, BigDecimal maxPrice,
            LocalDate dateTo, String sort, String order) {

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
        LocalDateTime end = (dateTo != null) ? dateTo.atTime(23, 59, 59) : date.atTime(23, 59, 59);

        FlightStatus queryStatus = status != null ? status : FlightStatus.SCHEDULED;
        List<FlightAggregate> flights = flightRepository.searchFlights(from, to, start, end, queryStatus);

        Counter.builder("flight.search")
                .description("Number of flight searches")
                .register(meterRegistry)
                .increment();

        record FlightAndInventory(FlightAggregate flight, SeatInventoryAggregate inv) {}

        Stream<FlightAndInventory> stream = flights.stream()
                .map(flight -> new FlightAndInventory(flight, seatInventoryRepository.findById(flight.getId()).orElse(null)))
                .filter(pair -> pair.inv() != null ? pair.inv().hasAvailableSeats(passengers) : pair.flight().getTotalSeats() >= passengers);

        // Apply advanced filters
        if (airline != null && !airline.isBlank()) {
            stream = stream.filter(pair -> pair.flight().getAirline() != null &&
                    pair.flight().getAirline().getCode().equalsIgnoreCase(airline));
        }

        if (minPrice != null) {
            stream = stream.filter(pair -> pair.flight().calculateCurrentPrice(pair.inv()).compareTo(minPrice) >= 0);
        }

        if (maxPrice != null) {
            stream = stream.filter(pair -> pair.flight().calculateCurrentPrice(pair.inv()).compareTo(maxPrice) <= 0);
        }

        List<FlightSearchResponse> responses = stream
                .map(pair -> FlightSearchResponse.of(pair.flight(), pair.inv()))
                .toList();

        // Apply Sorting
        if (sort != null) {
            Comparator<FlightSearchResponse> comparator = switch (sort.toLowerCase()) {
                case "price" -> Comparator.comparing(FlightSearchResponse::currentPrice);
                case "departuretime" -> Comparator.comparing(FlightSearchResponse::departureTime);
                case "arrivaltime" -> Comparator.comparing(FlightSearchResponse::arrivalTime);
                default -> Comparator.comparing(FlightSearchResponse::departureTime);
            };

            if ("desc".equalsIgnoreCase(order)) {
                comparator = comparator.reversed();
            }

            responses = responses.stream().sorted(comparator).toList();
        }

        return responses;
    }
}
