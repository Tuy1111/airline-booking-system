package com.abs.flightsearch.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seat_inventory")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SeatInventory {

    @Id
    @Column(name = "flight_id")
    private Long flightId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "flight_id")
    private Flight flight;

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
