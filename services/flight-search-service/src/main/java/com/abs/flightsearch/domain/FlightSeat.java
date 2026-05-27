package com.abs.flightsearch.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "flight_seat")
@IdClass(FlightSeatId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FlightSeat {

    @Id
    @Column(name = "flight_id")
    private Long flightId;

    @Id
    @Column(name = "seat_no", length = 5)
    private String seatNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "class", nullable = false, length = 20)
    private SeatClass seatClass;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatStatus status;

    @Column(name = "price_factor", precision = 4, scale = 2)
    private BigDecimal priceFactor;
}
