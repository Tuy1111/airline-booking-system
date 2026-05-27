package com.abs.flightsearch.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "flight")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_no", nullable = false, unique = true, length = 10)
    private String flightNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "airline_code", referencedColumnName = "code")
    private Airline airline;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(name = "base_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "aircraft_type", length = 20)
    private String aircraftType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FlightStatus status;

    @Version
    private Long version;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
