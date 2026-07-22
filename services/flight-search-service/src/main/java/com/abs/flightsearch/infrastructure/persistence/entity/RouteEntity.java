package com.abs.flightsearch.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "route")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "from_airport", referencedColumnName = "iata_code")
    private AirportEntity fromAirport;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "to_airport", referencedColumnName = "iata_code")
    private AirportEntity toAirport;

    @Column(name = "distance_km")
    private Integer distanceKm;
}
