package com.abs.booking.infrastructure.feign;

import com.abs.booking.infrastructure.feign.dto.FlightDetailResponse;
import com.abs.booking.infrastructure.feign.dto.FlightResponse;
import feign.Logger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * OpenFeign client gọi tới flight-search-service.
 *
 * - name = "flight-search-service" → tên service đăng ký trên Eureka
 * - path = "/api/v1/flights"       → base path của FlightInfoController
 *
 * Khi chạy, Feign sẽ gọi trực tiếp hoặc qua Eureka.
 */
@FeignClient(
        name = "flight-search-service",
        url = "http://localhost:8081",
        path = "/api/v1/flights",
        configuration = FlightSearchFeignClient.FeignLoggingConfig.class
)
public interface FlightSearchFeignClient {

    /**
     * Tìm chuyến bay: GET /api/v1/flights?from=HAN&to=SGN&date=2026-06-20
     */
    @GetMapping
    List<FlightResponse> searchFlights(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("date") String date
    );

    /**
     * Lấy chi tiết chuyến bay: GET /api/v1/flights/{id}
     */
    @GetMapping("/{id}")
    FlightDetailResponse getFlightById(@PathVariable("id") Long id);

    @Configuration
    class FeignLoggingConfig {
        @Bean
        public Logger.Level feignLoggerLevel() {
            return Logger.Level.FULL;
        }
    }
}
