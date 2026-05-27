# Airline Booking System

Hệ thống đặt vé máy bay theo kiến trúc **microservices** (Spring Boot 3.5 / Java 21 / Spring Cloud 2025.x).

Cấu trúc dự án mô phỏng theo mẫu `SE1912-JV_MSS301` (TalentHub).

## Architecture

```
airline-booking-system/
├── pom.xml                              # Parent BOM (Spring Boot + Spring Cloud + Lombok)
├── infra/
│   └── api-gateway/                     # Cổng vào hệ thống (port 8080)
├── shared/
│   └── common-web/                      # Module dùng chung (DTO, exception, util)
└── services/
    ├── flight-search-service/           # Tìm chuyến bay, lịch trình (port 8081)
    ├── booking-service/                 # Đặt chỗ, chọn ghế, giữ chỗ (port 8082)
    ├── user-service/                    # Hành khách, tài khoản (port 8083)
    ├── payment-service/                 # Thanh toán (port 8084)
    └── notification-service/            # Email/SMS xác nhận, nhắc bay (port 8085)
```

## Services & Ports

| Service                 | Port | Base path             | Trách nhiệm                                              |
| ----------------------- | ---- | --------------------- | -------------------------------------------------------- |
| api-gateway             | 8080 | `/`                   | Định tuyến, auth, rate-limit                             |
| flight-search-service   | 8081 | `/api/v1/flights`     | Lịch bay, route, availability                            |
| booking-service         | 8082 | `/api/v1/bookings`    | Đặt vé, chọn ghế, giữ ghế (xử lý concurrent booking)     |
| user-service            | 8083 | `/api/v1/users`       | Hồ sơ hành khách, tài khoản                              |
| payment-service         | 8084 | `/api/v1/payments`    | Thanh toán vé                                            |
| notification-service    | 8085 | `/api/v1/notifications` | Gửi xác nhận đặt vé, nhắc giờ bay                       |

## Build

```bash
# Từ thư mục gốc
mvn clean install
```

## Run từng service

Mỗi service là một Spring Boot app độc lập. Chạy bằng IDE hoặc CLI:

```bash
# Ví dụ flight-search-service
cd services/flight-search-service
mvn spring-boot:run
```

## Smoke test

Sau khi chạy, gọi endpoint ping của từng service:

```bash
curl http://localhost:8081/api/v1/flights/ping
curl http://localhost:8082/api/v1/bookings/ping
curl http://localhost:8083/api/v1/users/ping
curl http://localhost:8084/api/v1/payments/ping
curl http://localhost:8085/api/v1/notifications/ping

# Inter-service call (booking gọi sang flight-search)
curl http://localhost:8082/api/v1/bookings/ping-flight
```

Kết quả mong đợi:

```json
{ "status": "ok", "service": "flight-search-service", "message": "..." }
```

## Key Challenges (sẽ giải quyết ở các phase tiếp theo)

- **Concurrent booking** cho số ghế giới hạn → optimistic locking / Redis seat-hold
- **Pricing fluctuation** → caching + revalidation
- **Transactional consistency** giữa booking ↔ payment → Saga / Outbox pattern
- **Time-sensitive seat hold** → TTL (Redis) cho ghế đang giữ
- **Notification reliability** → message queue (Kafka/RabbitMQ) ở phase sau

## Stack

- Java 21
- Spring Boot 3.5.14
- Spring Cloud 2025.0.0
- Lombok 1.18.46
- Maven multi-module

## Roadmap gợi ý (cho các phase sau)

1. Domain models + persistence (JPA + DB cho mỗi service)
2. API gateway routing (Spring Cloud Gateway)
3. Service discovery (Eureka hoặc Consul)
4. Booking flow + concurrent seat hold
5. Payment integration + Saga
6. Async notification qua message broker
7. Observability (Actuator + tracing)
