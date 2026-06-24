package com.abs.user.application.dto;

import java.time.Instant;

/** A signed access token plus its metadata, produced by the {@code TokenIssuer} port. */
public record AuthToken(String accessToken, String tokenType, long expiresInSeconds, Instant expiresAt) {

    public static final String BEARER = "Bearer";

    public static AuthToken bearer(String accessToken, long expiresInSeconds, Instant expiresAt) {
        return new AuthToken(accessToken, BEARER, expiresInSeconds, expiresAt);
    }
}
