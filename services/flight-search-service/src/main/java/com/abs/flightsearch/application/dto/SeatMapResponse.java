//package com.abs.flightsearch.application.dto;
//
//import com.abs.flightsearch.domain.FlightSeat;
//import com.abs.flightsearch.domain.SeatClass;
//import com.abs.flightsearch.domain.SeatStatus;
//
//import java.math.BigDecimal;
//
//public record SeatMapResponse(
//        String seatNo,
//        SeatClass seatClass,
//        SeatStatus status,
//        BigDecimal priceFactor
//) {
//    public static SeatMapResponse of(FlightSeat seat) {
//        return new SeatMapResponse(
//                seat.getSeatNo(),
//                seat.getSeatClass(),
//                seat.getStatus(),
//                seat.getPriceFactor()
//        );
//    }
//}
