package com.abs.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Applies the shared {@code /api/v1} path prefix to every {@code @RestController}, matching the
 * convention used by the other services so controllers don't repeat the version prefix.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api/v1", clazz -> clazz.isAnnotationPresent(RestController.class));
    }
}
