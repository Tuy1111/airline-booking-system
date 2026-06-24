//package com.abs.flightsearch.application.dto;
//
//import com.abs.flightsearch.domain.Flight;
//import com.abs.flightsearch.domain.FlightStatus;
//import com.abs.flightsearch.domain.SeatInventory;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.time.LocalDateTime;
//
//public record FlightDetailResponse(
//        Long id,
//        String flightNo,
//        String airlineCode,
//        String airlineName,
//        String fromAirport,
//        String fromCity,
//        String toAirport,
//        String toCity,
//        LocalDateTime departureTime,
//        LocalDateTime arrivalTime,
//        BigDecimal basePrice,
//        BigDecimal currentPrice,
//        String aircraftType,
//        FlightStatus status,
//        int availableSeats,
//        int totalSeats,
//        int heldSeats,
//        int bookedSeats
//) {
//    public static FlightDetailResponse of(Flight f, SeatInventory inv) {
//        BigDecimal base = f.getBasePrice();
//        BigDecimal current = base;
//
//        if (inv != null && inv.getTotal() > 0) {
//            double occupancy = (double) inv.getBooked() / inv.getTotal();
//            BigDecimal multiplier = BigDecimal.valueOf(1.0 + 0.1 * occupancy);
//            current = base.multiply(multiplier).setScale(0, RoundingMode.CEILING);
//        }
//
//        return new FlightDetailResponse(
//                f.getId(),
//                f.getFlightNo(),
//                f.getAirline() != null ? f.getAirline().getCode() : "Unknown",
//                f.getAirline() != null ? f.getAirline().getName() : "Unknown",
//                f.getRoute() != null && f.getRoute().getFromAirport() != null ? f.getRoute().getFromAirport().getIataCode() : "Unknown",
//                f.getRoute() != null && f.getRoute().getFromAirport() != null ? f.getRoute().getFromAirport().getCity() : "Unknown",
//                f.getRoute() != null && f.getRoute().getToAirport() != null ? f.getRoute().getToAirport().getIataCode() : "Unknown",
//                f.getRoute() != null && f.getRoute().getToAirport() != null ? f.getRoute().getToAirport().getCity() : "Unknown",
//                f.getDepartureTime(),
//                f.getArrivalTime(),
//                base,
//                current,
//                f.getAircraftType(),
//                f.getStatus(),
//                inv != null ? inv.getAvailable() : f.getTotalSeats(),
//                inv != null ? inv.getTotal() : f.getTotalSeats(),
//                inv != null ? inv.getHeld() : 0,
//                inv != null ? inv.getBooked() : 0
//        );
//    }
//}
