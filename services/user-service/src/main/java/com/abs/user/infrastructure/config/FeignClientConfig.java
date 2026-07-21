package com.abs.user.infrastructure.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    private static final String AUTHORIZATION = "Authorization";
    private static final String USER_ID = "X-User-Id";

    @Bean
    RequestInterceptor authenticationHeaderForwardingInterceptor() {
        return template -> {
            if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
                return;
            }
            HttpServletRequest request = attributes.getRequest();
            forward(request, template, AUTHORIZATION);
            forward(request, template, USER_ID);
        };
    }

    private void forward(HttpServletRequest request, feign.RequestTemplate template, String headerName) {
        String value = request.getHeader(headerName);
        if (value != null && !value.isBlank()) {
            template.header(headerName, value);
        }
    }
}
