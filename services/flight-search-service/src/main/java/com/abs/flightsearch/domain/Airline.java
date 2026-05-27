package com.abs.flightsearch.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "airline")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Airline {

    @Id
    @Column(length = 2)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;
}
