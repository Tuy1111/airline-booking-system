package com.abs.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;

@EnableWebFluxSecurity
@Configuration
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.csrf(csrfSpec -> csrfSpec.disable())
                .authorizeExchange(exchanges ->
                        exchanges
                                // Cho phép tìm kiếm chuyến bay (Public) mà không cần Token
                                .pathMatchers("/public/**", "/api/v1/public/**", "/actuator/health").permitAll()
                                .anyExchange().authenticated()
                )
                .oauth2Login(org.springframework.security.config.Customizer.withDefaults())
                .oauth2ResourceServer(oauth2ResourceServerSpec ->
                        oauth2ResourceServerSpec.jwt(jwtSpec ->
                                jwtSpec.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .build();
    }

    private ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        var grantedConverter = new JwtGrantedAuthoritiesConverter();
        grantedConverter.setAuthoritiesClaimName("roles");
        grantedConverter.setAuthorityPrefix("ROLE_");

        var converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(
                jwt -> Flux.fromIterable(grantedConverter.convert(jwt)));
        return converter;
    }
}