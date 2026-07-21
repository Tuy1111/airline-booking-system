package com.abs.flightsearch.application.usecase;

import com.abs.flightsearch.domain.aggregate.AirlineAggregate;
import com.abs.flightsearch.domain.aggregate.AirportAggregate;
import com.abs.flightsearch.domain.aggregate.FlightAggregate;
import com.abs.flightsearch.domain.aggregate.RouteAggregate;
import com.abs.flightsearch.domain.aggregate.SeatInventoryAggregate;
import com.abs.flightsearch.domain.repository.AirportRepository;
import com.abs.flightsearch.domain.repository.FlightRepository;
import com.abs.flightsearch.domain.repository.SeatInventoryRepository;
import com.abs.flightsearch.domain.vo.FlightStatus;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SearchFlightsUseCaseTest {

    @Test
    void returnsAllFlightsWithoutFiltersAndNarrowsWithOptionalRouteFilter() {
        FlightRepository flights = mock(FlightRepository.class);
        AirportRepository airports = mock(AirportRepository.class);
        SeatInventoryRepository inventories = mock(SeatInventoryRepository.class);
        SearchFlightsUseCase useCase = new SearchFlightsUseCase(
                flights, airports, inventories, new SimpleMeterRegistry());

        FlightAggregate hanToSgn = flight(1L, "HAN", "SGN");
        FlightAggregate dadToSgn = flight(2L, "DAD", "SGN");
        when(flights.findAll()).thenReturn(List.of(hanToSgn, dadToSgn));
        when(inventories.findById(1L)).thenReturn(Optional.of(SeatInventoryAggregate.create(1L, 20)));
        when(inventories.findById(2L)).thenReturn(Optional.of(SeatInventoryAggregate.create(2L, 20)));
        when(airports.existsById("HAN")).thenReturn(true);

        assertEquals(2, useCase.execute(null, null, null, 1, null, null, null, null, null, "departureTime", "asc").size());
        assertEquals("HAN", useCase.execute("HAN", null, null, 1, null, null, null, null, null, "departureTime", "asc").getFirst().fromAirport());
    }

    @Test
    void onlyReturnsScheduledFlightsThatHaveNotDeparted() {
        FlightRepository flights = mock(FlightRepository.class);
        AirportRepository airports = mock(AirportRepository.class);
        SeatInventoryRepository inventories = mock(SeatInventoryRepository.class);
        SearchFlightsUseCase useCase = new SearchFlightsUseCase(
                flights, airports, inventories, new SimpleMeterRegistry());

        FlightAggregate departedByTime = flight(1L, "HAN", "SGN");
        departedByTime = FlightAggregate.builder()
                .id(departedByTime.getId())
                .flightNo(departedByTime.getFlightNo())
                .route(departedByTime.getRoute())
                .airline(departedByTime.getAirline())
                .departureTime(LocalDateTime.now().minusMinutes(1))
                .arrivalTime(LocalDateTime.now().plusHours(1))
                .totalSeats(20)
                .basePrice(BigDecimal.valueOf(900_000))
                .status(FlightStatus.SCHEDULED)
                .build();
        FlightAggregate delayed = FlightAggregate.builder()
                .id(2L)
                .flightNo("SS2")
                .route(flight(2L, "DAD", "SGN").getRoute())
                .airline(flight(2L, "DAD", "SGN").getAirline())
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .totalSeats(20)
                .basePrice(BigDecimal.valueOf(900_000))
                .status(FlightStatus.DELAYED)
                .build();

        when(flights.findAll()).thenReturn(List.of(departedByTime, delayed));
        when(inventories.findById(1L)).thenReturn(Optional.of(SeatInventoryAggregate.create(1L, 20)));
        when(inventories.findById(2L)).thenReturn(Optional.of(SeatInventoryAggregate.create(2L, 20)));

        assertTrue(useCase.execute(null, null, null, 1, null, null, null, null, null,
                "departureTime", "asc").isEmpty());
    }

    private FlightAggregate flight(long id, String from, String to) {
        AirportAggregate departure = AirportAggregate.builder().iataCode(from).city(from).name(from).country("VN").build();
        AirportAggregate arrival = AirportAggregate.builder().iataCode(to).city(to).name(to).country("VN").build();
        return FlightAggregate.builder()
                .id(id)
                .flightNo("SS" + id)
                .route(RouteAggregate.builder().fromAirport(departure).toAirport(arrival).distanceKm(900).build())
                .airline(AirlineAggregate.builder().code("SS").name("SkySwift").build())
                .departureTime(LocalDateTime.now().plusDays(id))
                .arrivalTime(LocalDateTime.now().plusDays(id).plusHours(2))
                .totalSeats(20)
                .basePrice(BigDecimal.valueOf(900_000))
                .status(FlightStatus.SCHEDULED)
                .build();
    }
}
