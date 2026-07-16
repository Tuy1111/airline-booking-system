package com.abs.api.filter;

import com.abs.api.utils.GatewayConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String correlationId = serverHttpRequest.getHeaders().getFirst(GatewayConstants.HEADER_CORRELATION_ID);
        String path = serverHttpRequest.getPath().value();
        String method = serverHttpRequest.getMethod().name();

        log.info("[PRE] Request {} to Path: {} with Correlation ID: {}", method, path, correlationId);

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    long executeTime = System.currentTimeMillis() - startTime;
                    int statusCode = (exchange.getResponse().getStatusCode() != null) ?
                            exchange.getResponse().getStatusCode().value() : 500;

                    log.info("[POST] Request {} to Path: {} finished with Status: {} in {} ms",
                            method, path, statusCode, executeTime);
                }));
    }

    @Override
    public int getOrder() {
        return GatewayConstants.ORDER_LOGGING_FILTER;
    }
}