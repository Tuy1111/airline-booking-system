package com.abs.flightsearch.domain;

import lombok.*;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class FlightSeatId implements Serializable {
    private Long flightId;
    private String seatNo;
}
