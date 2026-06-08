package com.abs.flightsearch.domain.aggregate;

import com.abs.flightsearch.domain.vo.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightAggregate {
    private Long id;
    private String flightNo;
    private RouteAggregate route;
    private AirlineAggregate airline;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer totalSeats;
    private BigDecimal basePrice;
    private String aircraftType;
    private FlightStatus status;
    private Long version;
    private LocalDateTime createdAt;
}
