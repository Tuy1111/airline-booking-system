package com.abs.booking.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * Ping chéo sang flight-search-service qua Eureka (chỉ cần TÊN SERVICE).
 *
 * Tách riêng khỏi FlightSearchFeignClient vì client kia có path="/api/v1",
 * mà /actuator/health KHÔNG nằm dưới prefix đó (WebConfig của flight-search
 * chỉ addPathPrefix cho class @RestController, actuator không bị prefix).
 *
 * contextId là BẮT BUỘC: đã có một @FeignClient khác cùng name="flight-search-service",
 * trùng name mà thiếu contextId sẽ fail lúc khởi động.
 */
@FeignClient(name = "flight-search-service", contextId = "flightSearchHealthClient")
public interface FlightSearchHealthClient {

    @GetMapping("/actuator/health")
    Map<String, Object> health();
}
