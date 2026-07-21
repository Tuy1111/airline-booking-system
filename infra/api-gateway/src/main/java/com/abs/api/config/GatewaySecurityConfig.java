package com.abs.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@EnableWebFluxSecurity
@Configuration
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers("/actuator/health", "/api/v1/payments/webhooks/sepay").permitAll()
                        .pathMatchers(HttpMethod.GET,
                                "/api/v1/flights/admin",
                                "/api/v1/bookings/admin").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET,
                                "/api/v1/flights/**",
                                "/api/v1/airports/**",
                                "/api/v1/airlines/**",
                                "/api/v1/routes/**").permitAll()
                        .pathMatchers(
                                "/api/v1/flights/**",
                                "/api/v1/airports/**",
                                "/api/v1/airlines/**",
                                "/api/v1/routes/**").hasRole("ADMIN")
                        .pathMatchers("/api/v1/bookings/**", "/api/v1/payments/**")
                                .access(nonAdmin())
                        .pathMatchers("/api/v1/notifications/me").authenticated()
                        .pathMatchers("/api/v1/notifications/**").hasRole("ADMIN")
                        .pathMatchers("/api/v1/users/me", "/api/v1/users/me/**").authenticated()
                        .pathMatchers("/api/v1/users/**").hasRole("ADMIN")
                        .anyExchange().authenticated())
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .build();
    }

    private ReactiveAuthorizationManager<AuthorizationContext> nonAdmin() {
        return (authentication, context) -> authentication
                .map(auth -> new AuthorizationDecision(
                        auth.isAuthenticated()
                                && auth.getAuthorities().stream()
                                .noneMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()))))
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

    private ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(
                jwt -> Flux.fromIterable(extractRealmRoles(jwt.getClaim("realm_access"))));
        return converter;
    }

    private Collection<SimpleGrantedAuthority> extractRealmRoles(Map<String, Object> realmAccess) {
        if (realmAccess == null || !(realmAccess.get("roles") instanceof List<?> roles)) {
            return List.of();
        }

        return roles.stream()
                .map(String::valueOf)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }
}
