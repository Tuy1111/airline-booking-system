package com.abs.flightsearch.domain.exception;

public class AirportNotFoundException extends RuntimeException {
    public AirportNotFoundException(String code) {
        super("Airport không tồn tại: " + code);
    }
}
