package com.abs.flightsearch.application.dto;

import com.abs.flightsearch.domain.aggregate.FlightAggregate;
import com.abs.flightsearch.domain.aggregate.FlightSeatAggregate;
import com.abs.flightsearch.domain.aggregate.SeatInventoryAggregate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record SeatInfoResponse(
        Long flightId,
        String seatNo,
        String seatClass,
        String status,
        BigDecimal price
) {
    public static SeatInfoResponse of(FlightSeatAggregate seat, FlightAggregate flight, SeatInventoryAggregate inv) {
        BigDecimal base = flight.getBasePrice();
        BigDecimal factor = seat.getPriceFactor() != null ? seat.getPriceFactor() : BigDecimal.ONE;
        
        double occupancy = 0.0;
        if (inv != null && inv.getTotal() > 0) {
            occupancy = (double) inv.getBooked() / inv.getTotal();
        }
        
        BigDecimal dynamicFactor = BigDecimal.valueOf(1.0 + 0.1 * occupancy);
        BigDecimal price = base.multiply(factor).multiply(dynamicFactor).setScale(0, RoundingMode.CEILING);

        return new SeatInfoResponse(
                seat.getFlightId(),
                seat.getSeatNo(),
                seat.getSeatClass().name(),
                seat.getStatus().name(),
                price
        );
    }
}
