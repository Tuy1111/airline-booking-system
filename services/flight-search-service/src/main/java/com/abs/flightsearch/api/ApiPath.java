package com.abs.flightsearch.api;

public final class ApiPath {
    public static final String FLIGHTS = "/flights";
    public static final String BY_ID = "/{id}";
    public static final String SEATS = "/{flightId}/seats";
    public static final String CHECK_SEAT = "/{flightId}/seats/{seatNo}";
    public static final String AIRPORTS = "/airports";
    public static final String AIRLINES = "/airlines";
    public static final String IMPORT = "/import";

    // P0
    public static final String HOLD_SEAT = "/{flightId}/seats/{seatNo}/hold";
    public static final String BOOK_SEAT = "/{flightId}/seats/{seatNo}/book";
    public static final String RELEASE_SEAT = "/{flightId}/seats/{seatNo}/release";

    // P1
    public static final String FLIGHT_STATUS = "/{id}/status";
    public static final String UPCOMING = "/upcoming";

    private ApiPath() {
    }
}
