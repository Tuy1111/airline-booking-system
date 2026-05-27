# Airline Booking System — Roadmap (8 tuần)

Kế hoạch triển khai dự án theo kiến trúc microservices.

| Tuần  | Giai đoạn                              | Mục tiêu chính                                                                 | Deliverables                                                                 |
| ----- | -------------------------------------- | ------------------------------------------------------------------------------ | ---------------------------------------------------------------------------- |
| 1–2   | **Setup & Thiết kế**                   | Chốt đề tài, thiết kế kiến trúc, chia service                                  | Sơ đồ kiến trúc, contract API, base code (đã xong ở step này)                |
| 2–5   | **Build từng service độc lập**         | Mỗi service: REST API + DB riêng (PostgreSQL/MySQL), CRUD + business logic   | 5 services chạy độc lập, có Swagger, test cơ bản                             |
| 6     | **Inter-service communication**        | Sync (REST/Feign) + Async (Kafka hoặc RabbitMQ)                                | Booking gọi Flight-search (sync), Booking → Notification (async qua broker)  |
| 7     | **Saga + Resilience + Observability**  | Saga cho luồng book↔payment, Circuit Breaker (Resilience4j), tracing/logging | Rollback tự động khi payment fail, dashboard health/metrics, distributed log |
| 8     | **Tích hợp, demo, báo cáo**            | End-to-end flow qua API Gateway, viết báo cáo, slide, quay demo                | Demo chạy đầy đủ, báo cáo PDF, slide thuyết trình                            |

---

## Tuần 1–2 · Setup & Thiết kế

**Mục tiêu:** Cả nhóm hiểu rõ kiến trúc và bắt đầu code song song.

**Việc cần làm:**
- [x] Khởi tạo multi-module Maven project (base hiện tại)
- [x] 5 services + api-gateway + common-web đã có skeleton + ping
- [ ] Vẽ sơ đồ kiến trúc (draw.io / excalidraw) — context diagram, container diagram
- [ ] Chốt **API contract** cho từng service (OpenAPI/Swagger)
- [ ] Chọn DB: gợi ý PostgreSQL cho tất cả (hoặc MongoDB cho `notification-service`)
- [ ] Phân chia owner cho từng service trong nhóm
- [ ] Tạo repo Git chung, branch strategy (gitflow đơn giản: `main` + `feature/*`)
- [ ] Setup Docker Compose cho DB local

**Output:**
- `docs/architecture.md` + sơ đồ
- `docs/api/*.yaml` (OpenAPI cho từng service)
- `docker-compose.yml` (Postgres, Kafka/RabbitMQ stub)

---

## Tuần 2–5 · Build từng service độc lập

**Mục tiêu:** Mỗi service tự chạy được, có DB riêng, có REST API thật (không còn ping).

### flight-search-service (Tuần 2–3)
- Entity: `Flight`, `Route`, `Airport`, `SeatInventory`
- API: `GET /api/v1/flights?from=&to=&date=`, `GET /api/v1/flights/{id}`
- Seed data 10–20 chuyến bay mẫu
- Index trên `(from, to, departure_date)` cho query nhanh

### user-service (Tuần 2–3)
- Entity: `User`, `Passenger`
- API: register, login (JWT), get profile, update passenger info
- BCrypt password, validation email/phone

### booking-service (Tuần 3–4) ⭐ **service phức tạp nhất**
- Entity: `Booking`, `Ticket`, `SeatHold`
- Trạng thái booking: `HELD → CONFIRMED → CANCELLED`
- **Seat hold với TTL** (Redis hoặc DB + scheduled job): giữ ghế 10 phút khi user chọn
- **Optimistic locking** (`@Version`) trên `SeatInventory` để tránh double-booking
- API: `POST /api/v1/bookings/hold`, `POST /api/v1/bookings/{id}/confirm`, `DELETE` cancel

### payment-service (Tuần 4)
- Entity: `Payment`, `Transaction`
- Mock payment gateway (không cần tích hợp thật) — random success/fail
- API: `POST /api/v1/payments`, idempotency key
- Trạng thái: `PENDING → SUCCESS / FAILED`

### notification-service (Tuần 4–5)
- Entity: `Notification` (log gì đã gửi)
- Gửi email (Spring Mail + MailHog cho dev) hoặc chỉ log ra console
- Template: booking confirmation, flight reminder

**Cuối tuần 5:** mỗi service test riêng OK qua Postman/Swagger.

---

## Tuần 6 · Inter-service communication

**Sync (REST):**
- `booking-service` → `flight-search-service`: check availability + lock seat
- `booking-service` → `user-service`: validate passenger exists
- Dùng `RestTemplate` (đã có sẵn) hoặc nâng cấp lên **OpenFeign**

**Async (Message Broker):**
- Chọn **Kafka** (recommended cho production-feel) hoặc **RabbitMQ** (đơn giản hơn)
- Topics/Queues:
  - `booking.confirmed` → `notification-service` consume → gửi email
  - `payment.completed` → `booking-service` consume → confirm booking
  - `payment.failed` → `booking-service` consume → release seat hold

**Việc cần làm:**
- [ ] Thêm Kafka/RabbitMQ vào `docker-compose.yml`
- [ ] Định nghĩa event schema trong `shared/common-web` (DTO: `BookingConfirmedEvent`, `PaymentCompletedEvent`...)
- [ ] Producer ở booking/payment, consumer ở notification/booking
- [ ] Test E2E: book vé → payment → email tự động được gửi

---

## Tuần 7 · Saga + Resilience + Observability

### Saga (luồng booking ↔ payment)
- Chọn **Choreography Saga** (đơn giản, đúng tinh thần microservice) thay vì Orchestration
- Flow chuẩn:
  1. `booking-service`: tạo booking `HELD`, publish `BookingHeldEvent`
  2. `payment-service`: consume → xử lý payment → publish `PaymentCompletedEvent` / `PaymentFailedEvent`
  3. `booking-service`: consume → `CONFIRMED` hoặc `CANCELLED` (release seat)
- Implement **Outbox pattern** để đảm bảo at-least-once delivery

### Resilience4j (Circuit Breaker)
- Bọc các call sync giữa services bằng `@CircuitBreaker`
- Fallback: trả lỗi thân thiện thay vì 500
- Retry với exponential backoff cho call tạm thời fail
- Timeout: 3s cho mỗi inter-service call

### Observability
- **Logging:** thêm `logback-spring.xml` + traceId/spanId trong mỗi log line
- **Tracing:** Spring Cloud Sleuth + Zipkin (hoặc Micrometer Tracing + Tempo)
- **Metrics:** Actuator + Prometheus + Grafana (dashboard sẵn của Spring Boot)
- **Health check:** `/actuator/health` đã có sẵn, cấu hình thêm DB/Kafka health

---

## Tuần 8 · Tích hợp, demo, báo cáo

### Tích hợp qua API Gateway
- Cấu hình Spring Cloud Gateway routing:
  - `/api/v1/flights/**` → flight-search-service
  - `/api/v1/bookings/**` → booking-service
  - `/api/v1/users/**` → user-service
  - `/api/v1/payments/**` → payment-service
- JWT auth filter ở gateway (validate token từ user-service)
- Rate limiting (Redis-based)

### Demo end-to-end
Kịch bản demo (10 phút):
1. User đăng ký + login → nhận JWT
2. Search chuyến bay HAN → SGN ngày 30/06
3. Chọn chuyến + chọn ghế → giữ ghế 10 phút
4. Thanh toán (mock) → nhận email confirmation
5. Demo case **2 user book cùng 1 ghế** → chỉ 1 thành công (concurrency)
6. Demo case **payment fail** → booking auto release seat
7. Show Grafana dashboard + Zipkin trace

### Báo cáo
- Slide thuyết trình (10–15 slides)
- Báo cáo PDF: intro, architecture, tech stack, challenges & solutions, demo screenshots, kết luận
- Quay video demo 5–7 phút (backup nếu live demo fail)
- Bảng phân công công việc rõ ràng cho từng thành viên

---

## Phân công gợi ý (cho nhóm 4–5 người)

| Thành viên | Tuần 2–5 (build service)       | Tuần 6–8 (integration)               |
| ---------- | ------------------------------ | ------------------------------------ |
| Member 1   | flight-search-service          | API Gateway + routing                |
| Member 2   | booking-service ⭐              | Saga + concurrency                   |
| Member 3   | user-service + JWT             | Resilience4j + Circuit Breaker       |
| Member 4   | payment-service                | Observability (Zipkin + Prometheus)  |
| Member 5   | notification-service           | Kafka/RabbitMQ + Docker Compose      |

(Nếu nhóm 4 người: gộp notification + payment cho 1 người)

---

## Risk & Mitigation

| Risk                                          | Mitigation                                              |
| --------------------------------------------- | ------------------------------------------------------- |
| Concurrent booking → double-book ghế          | Optimistic locking + DB unique constraint trên seat_id  |
| Payment fail nhưng seat đã confirmed          | Saga compensation → release seat hold                   |
| Kafka/Rabbit phức tạp, mất thời gian setup    | Bắt đầu sớm từ tuần 5, có fallback dùng REST async      |
| Thành viên chậm, kéo cả nhóm                  | Daily standup ngắn 10 phút, mỗi service có owner rõ     |
| Demo fail live                                | Quay video demo backup trước 1 ngày                     |
