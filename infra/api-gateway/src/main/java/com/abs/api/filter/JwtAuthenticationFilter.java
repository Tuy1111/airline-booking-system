package com.abs.api.filter;

import com.abs.api.utils.GatewayConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerWebExchange sanitizedExchange = sanitizeIdentityHeaders(exchange);

        return sanitizedExchange.getPrincipal()
                .ofType(JwtAuthenticationToken.class)
                .map(authentication -> enrichHeaders(sanitizedExchange, authentication.getToken()))
                .defaultIfEmpty(sanitizedExchange)
                .flatMap(chain::filter);
    }

    private ServerWebExchange sanitizeIdentityHeaders(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(headers -> {
                    headers.remove(GatewayConstants.HEADER_USER_ID);
                    headers.remove(GatewayConstants.HEADER_USER_NAME);
                    headers.remove(GatewayConstants.HEADER_USER_ROLES);
                })
                .build();
        return exchange.mutate().request(request).build();
    }

    private ServerWebExchange enrichHeaders(ServerWebExchange exchange, Jwt jwt) {
        Object userIdClaim = jwt.getClaim("user_id");
        String userId = userIdClaim == null
                ? numericIdFromSubject(jwt.getSubject())
                : String.valueOf(userIdClaim);
        String email = jwt.getClaimAsString("email");
        String roles = extractRealmRoles(jwt.getClaim("realm_access"));

        log.debug("Enriching headers from Keycloak - userId: {}, roles: {}", userId, roles);

        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(headers -> {
                    if (userId != null && !userId.isBlank()) {
                        headers.set(GatewayConstants.HEADER_USER_ID, userId);
                    }
                    if (email != null && !email.isBlank()) {
                        headers.set(GatewayConstants.HEADER_USER_NAME, email);
                    }
                    headers.set(GatewayConstants.HEADER_USER_ROLES, roles);
                })
                .build();
        return exchange.mutate().request(request).build();
    }

    private String numericIdFromSubject(String subject) {
        if (subject == null || subject.isBlank()) {
            return null;
        }
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(subject.getBytes(StandardCharsets.UTF_8));
            long value = ByteBuffer.wrap(digest).getLong() & Long.MAX_VALUE;
            return String.valueOf(value == 0 ? 1 : value);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }

    private String extractRealmRoles(Map<String, Object> realmAccess) {
        if (realmAccess == null || !(realmAccess.get("roles") instanceof List<?> roles)) {
            return "";
        }
        return roles.stream()
                .map(String::valueOf)
                .map(role -> "ROLE_" + role)
                .collect(Collectors.joining(","));
    }

    @Override
    public int getOrder() {
        return GatewayConstants.ORDER_JWT_AUTH_FILTER;
    }
}
