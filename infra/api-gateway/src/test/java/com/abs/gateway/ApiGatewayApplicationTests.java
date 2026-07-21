package com.abs.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "KEYCLOAK_URL=http://localhost:8180",
        "KEYCLOAK_INTERNAL_URL=http://localhost:8180",
        "REDIS_HOST=localhost",
        "REDIS_PORT=6379",
        "CORS_ALLOWED_ORIGINS=http://localhost:5173",
        "EUREKA_URL=http://localhost:8761/eureka",
        "ZIPKIN_ENDPOINT=http://localhost:9411/api/v2/spans",
        "eureka.client.enabled=false",
        "management.tracing.enabled=false"
})
class ApiGatewayApplicationTests {

    @Test
    void contextLoads() {
    }

}
