package com.abs.notification.api.controller;

public final class ApiPath {
    public static final String BASE = "/api/v1";
    public static final String NOTIFICATIONS = BASE + "/notifications";

    public static final String BY_ID = "/{id}";
    public static final String BY_USER = "/user/{userId}";
    public static final String BY_USER_LEGACY = "/by-user/{userId}";

    private ApiPath() {
    }
}
