package com.abs.flightsearch.application.dto;

import com.abs.flightsearch.domain.Flight;
import com.abs.flightsearch.domain.FlightSeat;
import com.abs.flightsearch.domain.SeatInventory;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record SeatInfoResponse(
        Long flightId,
        String seatNo,
        String seatClass,
        String status,
        BigDecimal price
) {
    public static SeatInfoResponse of(FlightSeat seat, Flight flight, SeatInventory inv) {
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
