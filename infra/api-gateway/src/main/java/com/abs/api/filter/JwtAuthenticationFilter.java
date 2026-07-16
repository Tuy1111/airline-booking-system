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
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getPrincipal()
                .flatMap(principal -> {
                    if (principal instanceof JwtAuthenticationToken) {
                        Jwt jwt = ((JwtAuthenticationToken) principal).getToken();
                        return Mono.just(enrichHeaders(exchange,
                                jwt.getSubject(),
                                jwt.getClaimAsString("email"),
                                jwt.getClaimAsStringList("roles")));
                    } else if (principal instanceof org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) {
                        org.springframework.security.oauth2.core.user.OAuth2User user = ((org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) principal).getPrincipal();
                        String userId = user.getAttribute("sub");
                        String email = user.getAttribute("email");
                        // Tùy thuộc vào cấu hình Keycloak, roles có thể nằm trong realm_access.roles
                        java.util.Map<String, Object> realmAccess = user.getAttribute("realm_access");
                        List<String> roles = null;
                        if (realmAccess != null && realmAccess.containsKey("roles")) {
                            roles = (List<String>) realmAccess.get("roles");
                        }
                        return Mono.just(enrichHeaders(exchange, userId, email, roles));
                    }
                    return Mono.empty();
                })
                .cast(ServerWebExchange.class)
                .switchIfEmpty(Mono.just(exchange))
                .flatMap(chain::filter);
    }

    private ServerWebExchange enrichHeaders(ServerWebExchange exchange, String userId, String email, List<String> roles) {
        String rolesAsString = (roles != null) ? roles.stream()
                .map(role -> "ROLE_" + role)
                .collect(Collectors.joining(",")) : "";

        log.debug("Enriching headers for Airline Services - userId: {}, roles: {}", userId, rolesAsString);

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(GatewayConstants.HEADER_USER_ID, userId)
                .header(GatewayConstants.HEADER_USER_ROLES, rolesAsString)
                .header(GatewayConstants.HEADER_USER_NAME, email)
                .build();

        return exchange.mutate().request(mutatedRequest).build();
    }

    @Override
    public int getOrder() {
        return GatewayConstants.ORDER_JWT_AUTH_FILTER;
    }
}