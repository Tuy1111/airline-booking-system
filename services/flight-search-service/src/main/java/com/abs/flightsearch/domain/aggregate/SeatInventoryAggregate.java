package com.abs.flightsearch.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatInventoryAggregate {
    private Long flightId;
    private Integer total;
    private Integer available;
    private Integer held;
    private Integer booked;
    private Long version;
}
