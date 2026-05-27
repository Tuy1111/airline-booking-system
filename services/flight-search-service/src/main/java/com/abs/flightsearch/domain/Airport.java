package com.abs.flightsearch.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "airport")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Airport {

    @Id
    @Column(name = "iata_code", length = 3)
    private String iataCode;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 50)
    private String country;
}
