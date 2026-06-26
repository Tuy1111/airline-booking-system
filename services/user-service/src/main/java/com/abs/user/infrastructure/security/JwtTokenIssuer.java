package com.abs.user.infrastructure.security;

import com.abs.user.application.dto.AuthToken;
import com.abs.user.application.port.TokenIssuer;
import com.abs.user.domain.aggregate.UserAggregate;
import com.abs.user.domain.vo.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * {@link TokenIssuer} backed by JJWT, signing a compact HS256 JWT.
 *
 * <p>The token's subject is the user id; {@code email} and {@code roles} are added as claims so a
 * resource server / gateway can authorise without another round-trip. The HMAC secret and TTL come
 * from configuration ({@code security.jwt.*}); the secret must be at least 256 bits for HS256.
 */
@Component
@Slf4j
public class JwtTokenIssuer implements TokenIssuer {

    private final SecretKey signingKey;
    private final long ttlSeconds;
    private final String issuer;

    public JwtTokenIssuer(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.ttl-seconds:3600}") long ttlSeconds,
            @Value("${security.jwt.issuer:user-service}") String issuer) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.ttlSeconds = ttlSeconds;
        this.issuer = issuer;
    }

    @Override
    public AuthToken issue(UserAggregate user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(ttlSeconds);
        List<String> roles = user.getRoles().stream().map(Role::name).toList();

        String jwt = Jwts.builder()
                .issuer(issuer)
                .subject(String.valueOf(user.getId().value()))
                .claim("email", user.getEmail().value())
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();

        log.info("Issued JWT: userId={}, roles={}, expiresAt={}", user.getId(), roles, expiresAt);
        return AuthToken.bearer(jwt, ttlSeconds, expiresAt);
    }
}
