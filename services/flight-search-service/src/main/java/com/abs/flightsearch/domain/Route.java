package com.abs.flightsearch.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "route")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_airport", referencedColumnName = "iata_code")
    private Airport fromAirport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_airport", referencedColumnName = "iata_code")
    private Airport toAirport;

    @Column(name = "distance_km")
    private Integer distanceKm;
}
