package com.abs.payment.api.controller;

public final class ApiPath {
    public static final String BASE = "/api/v1";
    public static final String PAYMENTS = BASE + "/payments";
    public static final String WEBHOOK_SEPAY = PAYMENTS + "/webhooks/sepay";

    public static final String BY_ID = "/{id}";
    public static final String BY_CODE = "/by-code/{code}";
    public static final String BY_BOOKING = "/by-booking/{bookingId}";

    private ApiPath() {
    }
}
