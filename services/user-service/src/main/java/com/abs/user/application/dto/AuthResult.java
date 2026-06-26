package com.abs.user.application.dto;

import java.time.Instant;

/** Result of register / login: the issued JWT plus the user projection. */
public record AuthResult(
        String accessToken,
        String tokenType,
        long expiresInSeconds,
        Instant expiresAt,
        UserView user) {

    public static AuthResult of(AuthToken token, UserView user) {
        return new AuthResult(
                token.accessToken(),
                token.tokenType(),
                token.expiresInSeconds(),
                token.expiresAt(),
                user);
    }
}
