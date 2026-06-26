package com.abs.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Service template theo kiến trúc Hexagonal (Ports &amp; Adapters) + DDD chiến thuật.
 * Component scan từ {@code com.abs.template} bao trùm mọi tầng: domain / application / adapter.
 */
@SpringBootApplication
public class ServiceTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceTemplateApplication.class, args);
    }
}
