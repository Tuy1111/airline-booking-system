package com.abs.flightsearch.domain.exception;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(Long id) {
        super("Flight không tồn tại với id: " + id);
    }
}
