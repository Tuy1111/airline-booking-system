package com.abs.flightsearch.api;

public final class ApiPath {
    public static final String FLIGHTS = "/flights";
    public static final String BY_ID = "/{id}";
    public static final String SEATS = "/{flightId}/seats";
    public static final String CHECK_SEAT = "/{flightId}/seats/{seatNo}";
    public static final String AIRPORTS = "/airports";
    public static final String AIRLINES = "/airlines";
    public static final String IMPORT = "/import";

    private ApiPath() {
    }
}
