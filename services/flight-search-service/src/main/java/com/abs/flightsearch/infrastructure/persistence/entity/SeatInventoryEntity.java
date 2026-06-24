package com.abs.flightsearch.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seat_inventory")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SeatInventoryEntity {

    @Id
    @Column(name = "flight_id")
    private Long flightId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "flight_id")
    private FlightEntity flight;

    @Column(nullable = false)
    private Integer total;

    @Column(nullable = false)
    private Integer available;

    @Column(nullable = false)
    private Integer held;

    @Column(nullable = false)
    private Integer booked;

    @Version
    private Long version;
}
