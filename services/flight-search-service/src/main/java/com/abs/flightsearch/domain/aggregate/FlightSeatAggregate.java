package com.abs.flightsearch.domain.aggregate;

import com.abs.flightsearch.domain.vo.SeatClass;
import com.abs.flightsearch.domain.vo.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSeatAggregate {
    private Long flightId;
    private String seatNo;
    private SeatClass seatClass;
    private SeatStatus status;
    private BigDecimal priceFactor;
}
