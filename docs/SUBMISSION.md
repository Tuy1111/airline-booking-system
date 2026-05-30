# Airline Booking System — Deliverables for MSS301

Hai phần bắt buộc nộp theo yêu cầu môn học:

- [Phần A · Database Design](#phần-a--database-design)
- [Phần B · Communication & Architecture Design](#phần-b--communication--architecture-design)

---

# Phần A · Database Design

## A.1. Tổng quan chiến lược dữ liệu

Hệ thống áp dụng **Database-per-Service pattern** — nguyên tắc nền tảng của microservices:

- Mỗi trong 5 microservice **sở hữu một database riêng**, không service nào được phép truy vấn trực tiếp DB của service khác.
- Tham chiếu giữa các service được lưu dưới dạng **logical foreign key** — chỉ là cột `BIGINT` (vd `user_id`, `flight_id`) không có constraint vật lý qua DB.
- Lý do: đảm bảo **loose coupling** ở tầng dữ liệu — đổi schema 1 service không break các service khác; mỗi service có thể chọn DB engine khác nhau (nhưng dự án thống nhất PostgreSQL 16 cho đơn giản).

```
┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│   flight_db      │  │    user_db       │  │   booking_db     │
│  6 bảng          │  │   3 bảng         │  │   3 bảng + Redis │
└──────────────────┘  └──────────────────┘  └──────────────────┘
┌──────────────────┐  ┌──────────────────┐
│   payment_db     │  │   notify_db      │
│   4 bảng         │  │   3 bảng         │
└──────────────────┘  └──────────────────┘

  Postgres 16 (cùng 1 server, 5 logical database tách biệt)
  Redis 7 (chỉ booking-service sử dụng — distributed lock cho seat-hold)
```

## A.2. ERD chi tiết từng database

### A.2.1. flight_db (flight-search-service)

```mermaid
erDiagram
    AIRPORT ||--o{ ROUTE : "from"
    AIRPORT ||--o{ ROUTE : "to"
    AIRLINE ||--o{ FLIGHT : operates
    ROUTE   ||--o{ FLIGHT : has
    FLIGHT  ||--|| SEAT_INVENTORY : tracks
    FLIGHT  ||--o{ FLIGHT_SEAT : "has seats"

    AIRPORT {
        varchar iata_code PK "HAN, SGN, DAD"
        varchar name
        varchar city
        varchar country
    }
    AIRLINE {
        varchar code PK "VN, VJ, QH"
        varchar name
    }
    ROUTE {
        bigint id PK
        varchar from_airport FK
        varchar to_airport FK
        int distance_km
    }
    FLIGHT {
        bigint id PK
        varchar flight_no UK "VN201"
        bigint route_id FK
        varchar airline_code FK
        timestamp departure_time
        timestamp arrival_time
        int total_seats
        decimal base_price
        varchar aircraft_type
        varchar status "SCHEDULED|DELAYED|CANCELLED|DEPARTED"
        bigint version "@Version optimistic lock"
        timestamp created_at
    }
    SEAT_INVENTORY {
        bigint flight_id PK,FK
        int total
        int available
        int held
        int booked
        bigint version "@Version optimistic lock"
    }
    FLIGHT_SEAT {
        bigint flight_id PK,FK
        varchar seat_no PK "12A, 12B"
        varchar class "ECONOMY|BUSINESS|FIRST"
        varchar status "AVAILABLE|HELD|BOOKED"
        decimal price_factor "1.0-2.5"
    }
```

**Vai trò các bảng:**

| Bảng              | Vai trò chính                                                                |
| ----------------- | ---------------------------------------------------------------------------- |
| `airport`         | Danh mục sân bay (master data, ít thay đổi)                                   |
| `airline`         | Danh mục hãng bay                                                             |
| `route`           | Đường bay (A → B), distance dùng để tính giá / thời gian bay                  |
| `flight`          | Chuyến bay cụ thể, có ngày giờ và giá gốc                                      |
| `seat_inventory`  | Bảng đếm tổng hợp: tổng, còn trống, đang giữ, đã bán — query nhanh khi search |
| `flight_seat`     | Trạng thái từng ghế cụ thể, cần cho UI chọn ghế và chống double-book          |

### A.2.2. user_db (user-service)

```mermaid
erDiagram
    USERS ||--|| PASSENGER : "owns profile"
    USERS ||--o{ USER_ROLE : has

    USERS {
        bigint id PK
        varchar email UK
        varchar password_hash "BCrypt"
        varchar status "ACTIVE|LOCKED|DELETED"
        timestamp created_at
        timestamp last_login_at
    }
    PASSENGER {
        bigint user_id PK,FK
        varchar full_name
        varchar phone
        date date_of_birth
        varchar gender "MALE|FEMALE|OTHER"
        varchar passport_no
        varchar nationality "ISO 3166-1"
    }
    USER_ROLE {
        bigint user_id PK,FK
        varchar role PK "USER|ADMIN"
    }
```

**Quyết định thiết kế:** `users` và `passenger` quan hệ 1-1, share PK (`@MapsId` trong JPA) — tách thành 2 bảng để:

- `users` chỉ chứa thông tin authentication, dễ thay đổi (đổi mật khẩu, lock account).
- `passenger` chứa thông tin hành khách (KYC) cần cho việc bay — ít thay đổi.

### A.2.3. booking_db (booking-service) ⭐ schema cốt lõi

```mermaid
erDiagram
    BOOKING ||--|{ BOOKING_ITEM : "contains seats"
    BOOKING ||--o{ OUTBOX_EVENT : "emits events"

    BOOKING {
        bigint id PK
        varchar booking_code UK "BK20260530001"
        bigint user_id "→ user_db (logical FK)"
        bigint flight_id "→ flight_db (logical FK)"
        varchar status "HELD|CONFIRMED|CANCELLED|EXPIRED|REFUNDED"
        decimal total_amount
        varchar currency "VND"
        timestamp held_at
        timestamp expires_at "HELD: now + 10min"
        timestamp confirmed_at
        timestamp cancelled_at
        varchar payment_id "→ payment_db"
        bigint version "@Version"
        timestamp created_at
    }
    BOOKING_ITEM {
        bigint id PK
        bigint booking_id FK
        varchar seat_no "12A"
        varchar passenger_name
        varchar passenger_passport
        decimal price
    }
    OUTBOX_EVENT {
        bigint id PK
        varchar aggregate_type "Booking"
        bigint aggregate_id
        varchar event_type "BookingHeld|BookingConfirmed|BookingCancelled"
        jsonb payload "snapshot JSON"
        varchar status "PENDING|SENT|FAILED"
        int retry_count
        timestamp created_at
        timestamp sent_at
    }
```

**Booking lifecycle (state machine):**

```mermaid
stateDiagram-v2
    [*] --> HELD : POST /hold + Redis lock + TTL 10min

    HELD --> CONFIRMED : PaymentCompleted event
    HELD --> CANCELLED : PaymentFailed event (compensation)
    HELD --> EXPIRED   : TTL hết, scheduled job dọn

    CONFIRMED --> REFUNDED : user request refund

    CONFIRMED --> [*]
    CANCELLED --> [*]
    EXPIRED   --> [*]
    REFUNDED  --> [*]
```

**Redis schema (đi kèm booking_db):**

| Key                           | Value                  | TTL   | Mục đích                       |
| ----------------------------- | ---------------------- | ----- | ------------------------------ |
| `seat:{flightId}:{seatNo}`    | `{bookingId,userId}`   | 10min | Distributed lock chống tranh chấp ghế |
| `idempotency:{key}`           | `{bookingId}`          | 24h   | Chống double-submit            |
| `flight:price:{flightId}`     | `{currentPrice}`       | 5min  | Cache giá đã tính              |

### A.2.4. payment_db (payment-service)

```mermaid
erDiagram
    PAYMENT ||--o{ TRANSACTION : "gateway calls"
    PAYMENT ||--o{ REFUND : "may have"
    PAYMENT ||--o{ OUTBOX_EVENT : "emits"

    PAYMENT {
        bigint id PK
        varchar payment_code UK "PM20260530001"
        bigint booking_id "→ booking_db"
        bigint user_id "→ user_db"
        decimal amount
        varchar currency
        varchar method "CARD|WALLET|BANK_TRANSFER"
        varchar status "PENDING|SUCCESS|FAILED"
        varchar idempotency_key UK "chống double-submit"
        timestamp created_at
        timestamp completed_at
    }
    TRANSACTION {
        bigint id PK
        bigint payment_id FK
        varchar gateway_txn_id
        text gateway_response
        varchar status
        timestamp created_at
    }
    REFUND {
        bigint id PK
        bigint payment_id FK
        decimal amount
        varchar reason
        varchar status
        timestamp created_at
    }
    OUTBOX_EVENT {
        bigint id PK
        varchar event_type "PaymentCompleted|PaymentFailed"
        jsonb payload
        varchar status
    }
```

**Tại sao tách `payment` và `transaction`?** Một `payment` có thể có nhiều lần thử (retry) → mỗi lần gọi gateway lưu 1 `transaction` để audit; trạng thái cuối ở `payment.status`.

### A.2.5. notify_db (notification-service)

```mermaid
erDiagram
    NOTIFICATION_TEMPLATE ||--o{ NOTIFICATION : "rendered from"

    NOTIFICATION_TEMPLATE {
        varchar code PK "BOOKING_CONFIRMED"
        varchar locale PK "vi|en"
        varchar channel PK "EMAIL|SMS|PUSH"
        varchar subject
        text body "Mustache template"
    }
    NOTIFICATION {
        bigint id PK
        varchar template_code FK
        bigint user_id "→ user_db"
        varchar channel
        varchar recipient "email/phone"
        jsonb variables "{passengerName, bookingCode, ...}"
        varchar status "PENDING|SENT|FAILED"
        int retry_count
        text error_message
        timestamp created_at
        timestamp sent_at
    }
    FLIGHT_REMINDER_LOG {
        bigint booking_id PK "chống gửi reminder trùng"
        bigint flight_id
        timestamp sent_at
    }
```

## A.3. Mapping bảng ↔ chức năng nghiệp vụ

| Use case nghiệp vụ              | Bảng được ghi vào                                                          |
| ------------------------------- | --------------------------------------------------------------------------- |
| Đăng ký tài khoản                | `users`, `passenger`, `user_role`                                          |
| Search flight                    | (read only) `flight`, `route`, `seat_inventory`                            |
| Hold seat ⭐                     | `booking` (status=HELD), `booking_item`, `outbox_event` (booking_db); Redis SETNX |
| Make payment                     | `payment` (status=PENDING), `transaction` (gateway call), `outbox_event` (payment_db) |
| Confirm booking (saga step)      | `booking` (HELD → CONFIRMED), `outbox_event` — triggered by PaymentCompleted |
| Cancel/compensation              | `booking` (CANCELLED), `refund`, `outbox_event` × 2 (booking + payment)     |
| Release expired hold (cron)      | `booking` (HELD → EXPIRED), release Redis key                              |
| Send booking confirmation email  | `notification` (PENDING → SENT) — render từ `notification_template`         |
| Send flight reminder (cron)      | `notification`, `flight_reminder_log` (chống gửi trùng)                     |

## A.4. Concurrency Strategy — chống double-booking

Đây là **bài toán quan trọng nhất** của đề tài. Dùng **2 lớp bảo vệ chồng nhau**:

```
   Lớp 1 — Redis Distributed Lock (nhanh, < 1 ms)
   ─────────────────────────────────────────────────
   SETNX seat:{flightId}:{seatNo}  TTL=10min  → atomic
        ↓ thắng
        ↓
   Lớp 2 — Database Optimistic Locking (backup)
   ─────────────────────────────────────────────────
   UPDATE booking SET ... WHERE id=? AND version=?
   UNIQUE INDEX (booking_id, seat_no) ON booking_item
        ↓
   INSERT booking, INSERT booking_item — atomic transaction
```

**Tại sao cần cả 2 lớp?**

- Redis nhanh nhưng có thể bị flush / lose memory.
- DB optimistic lock chậm hơn nhưng an toàn tuyệt đối.
- Khi cả 2 cùng hoạt động: hiệu năng cao + đảm bảo correctness.

## A.5. Các quyết định thiết kế DB quan trọng

| Quyết định                                                          | Lý do                                                                             |
| ------------------------------------------------------------------- | --------------------------------------------------------------------------------- |
| `@Version` trên Flight, SeatInventory, Booking                       | Optimistic locking — chống lost update khi concurrent write                       |
| Partial index `WHERE status = 'HELD'` trên `booking.expires_at`      | Job scan expired booking chạy nhanh, index nhỏ hơn ~80%                            |
| `outbox_event` (JSONB payload) ở booking + payment                   | Outbox pattern — at-least-once delivery cho Kafka (không mất event)                |
| `idempotency_key` UNIQUE trên `payment`                              | Chống double-submit khi user bấm Pay 2 lần                                          |
| `CHECK CONSTRAINT` cho mọi cột enum                                  | Type safety ở DB level — tránh corrupt data do bug code                            |
| `BIGINT user_id` (logical FK, không có constraint)                   | Tuân thủ Database-per-Service — services độc lập về schema                          |
| `flight_seat` riêng + bảng đếm `seat_inventory`                      | Tách giữa "trạng thái từng ghế" và "tổng hợp đếm" để query search bay nhanh         |
| Flyway migrations (`V1__init_schema.sql`, …)                         | Schema được version hoá, deploy lặp lại được, dễ rollback                           |
| `notification_template` có composite PK (code, locale, channel)      | Hỗ trợ đa ngôn ngữ + đa kênh (Email, SMS) — chỉ cần thêm row mới, không phải code   |

---

# Phần B · Communication & Architecture Design

## B.1. Kiến trúc tổng quan

Hệ thống áp dụng **Microservices Architecture** với các nguyên tắc:

| Nguyên tắc                       | Thực thi trong dự án                                                       |
| -------------------------------- | -------------------------------------------------------------------------- |
| Single Responsibility per service | Mỗi service phụ trách 1 bounded context: flight, user, booking, payment, notification |
| Database-per-Service              | 5 DB tách biệt, không cross-DB query                                       |
| API Gateway as single entrypoint  | `api-gateway` :8080 — định tuyến, xác thực JWT, rate-limit                  |
| Independent deployment            | Mỗi service có Dockerfile riêng, deploy độc lập, không break service khác   |
| Polyglot persistence              | Postgres cho hầu hết; Redis cho distributed lock (booking-service)          |
| Event-driven where appropriate    | Kafka cho event bất đồng bộ (booking, payment, notification)                |

## B.2. Container Diagram

```mermaid
flowchart TB
    User([👤 User / Mobile App])

    subgraph Edge["🌐 Edge Layer"]
        GW[API Gateway<br/>:8080<br/>Spring Cloud Gateway<br/>JWT + RateLimit]
    end

    subgraph Services["📦 Microservices"]
        FS[flight-search-service<br/>:8081]
        BK[booking-service<br/>:8082 ⭐]
        US[user-service<br/>:8083]
        PM[payment-service<br/>:8084]
        NT[notification-service<br/>:8085]
    end

    subgraph Data["💾 Data Layer"]
        DB1[(Postgres<br/>flight_db)]
        DB2[(Postgres<br/>booking_db)]
        DB3[(Postgres<br/>user_db)]
        DB4[(Postgres<br/>payment_db)]
        DB5[(Postgres<br/>notify_db)]
        REDIS[(Redis<br/>seat-hold)]
    end

    subgraph Broker["📨 Message Broker"]
        KAFKA{{Apache Kafka}}
    end

    subgraph Obs["📊 Observability"]
        ZIP[Zipkin]
        PROM[Prometheus + Grafana]
    end

    EXT[📧 SMTP / MailHog]

    User --> GW
    GW --> FS & BK & US & PM

    FS --> DB1
    BK --> DB2
    BK --> REDIS
    US --> DB3
    PM --> DB4
    NT --> DB5

    BK -.sync REST.-> FS
    BK -.sync REST.-> US

    BK ==>|publish| KAFKA
    PM ==>|publish| KAFKA
    KAFKA ==>|consume| BK
    KAFKA ==>|consume| NT

    NT --> EXT

    FS & BK & US & PM & NT -.tracing.-> ZIP
    FS & BK & US & PM & NT -.metrics.-> PROM
```

## B.3. Mô tả từng service

| Service                  | Port  | Domain Responsibility                                                    | Sync Calls In        | Sync Calls Out                | Events Published                                          | Events Consumed                            |
| ------------------------ | ----- | ------------------------------------------------------------------------ | -------------------- | ----------------------------- | --------------------------------------------------------- | ------------------------------------------ |
| **api-gateway**          | 8080  | Routing, authentication, rate-limit                                       | All client requests  | Tất cả 5 services             | —                                                         | —                                          |
| **flight-search-service**| 8081  | Lịch chuyến bay, đường bay, sân bay, ghế khả dụng                         | API Gateway, booking | —                             | —                                                         | —                                          |
| **booking-service** ⭐    | 8082  | Giữ ghế (hold), xác nhận booking, hủy booking                            | API Gateway          | flight-search, user           | `booking.held`, `booking.confirmed`, `booking.cancelled`  | `payment.completed`, `payment.failed`      |
| **user-service**         | 8083  | Đăng ký, đăng nhập, JWT, hồ sơ hành khách                                 | API Gateway, booking | —                             | `user.registered`                                          | —                                          |
| **payment-service**      | 8084  | Xử lý thanh toán, hoàn tiền                                              | API Gateway          | (gọi mock payment gateway)    | `payment.completed`, `payment.failed`, `refund.completed` | `booking.held` (theo dõi để biết cần pay)  |
| **notification-service** | 8085  | Gửi email/SMS xác nhận, nhắc nhở                                          | —                    | (gọi SMTP/SMS provider)       | —                                                         | `booking.confirmed`, `booking.cancelled`, `payment.failed` |

## B.4. Communication Patterns — chi tiết

### B.4.1. Synchronous Communication (REST over HTTP/JSON)

**Khi nào dùng:** Caller cần kết quả ngay lập tức để tiếp tục logic.

**Ví dụ luồng:**

```mermaid
sequenceDiagram
    actor U as User
    participant GW as API Gateway
    participant BK as booking-service
    participant FS as flight-search-service
    participant US as user-service

    U->>GW: POST /api/v1/bookings/hold (JWT, flightId, seat)
    GW->>GW: validate JWT
    GW->>BK: forward request + userId in header

    BK->>FS: GET /api/v1/flights/{flightId}/seats/{seat}
    Note over FS: trả về availability + giá hiện tại
    FS-->>BK: SeatAvailabilityDTO

    BK->>US: GET /api/v1/users/{userId}
    US-->>BK: UserDTO (xác nhận user tồn tại + active)

    BK->>BK: Redis SETNX + INSERT booking
    BK-->>U: 201 Created + bookingId + holdExpiresAt
```

**Đặc điểm:**

- Dùng `RestTemplate` (đã setup trong base code) — có thể nâng cấp lên **OpenFeign** ở phase Tuần 6 để code declarative hơn.
- Mọi call đều có **timeout 3 giây** và bọc bởi **Circuit Breaker** (Resilience4j).
- DTO định nghĩa rõ trong `shared/common-web` module để cả 2 phía cùng dùng.

### B.4.2. Asynchronous Communication (Apache Kafka)

**Khi nào dùng:** Caller không cần đợi kết quả ngay; nhiều consumer cùng phản ứng với 1 event.

**Topic design:**

| Topic                  | Producer       | Consumer(s)                  | Partition Key | Purpose                                       |
| ---------------------- | -------------- | ---------------------------- | ------------- | --------------------------------------------- |
| `booking.held`         | booking-svc    | payment-svc                  | `bookingId`   | Báo cho payment biết có booking chờ pay        |
| `booking.confirmed`    | booking-svc    | notification-svc             | `bookingId`   | Trigger gửi email xác nhận                     |
| `booking.cancelled`    | booking-svc    | notification-svc             | `bookingId`   | Trigger gửi email hủy                          |
| `payment.completed`    | payment-svc    | booking-svc                  | `bookingId`   | Báo booking confirm chính thức                 |
| `payment.failed`       | payment-svc    | booking-svc, notification-svc | `bookingId`   | Trigger compensation: hủy booking + email fail |

**Tại sao partition theo `bookingId`?** Đảm bảo tất cả event của cùng 1 booking đi vào cùng 1 partition → consumer xử lý theo thứ tự đúng (held → completed → confirmed), tránh race condition.

**Event payload schema (chia sẻ qua `shared/common-web`):**

```java
public record BookingHeldEvent(
    Long bookingId,
    String bookingCode,
    Long userId,
    Long flightId,
    String seatNo,
    BigDecimal amount,
    String currency,
    Instant heldAt,
    Instant expiresAt
) {}

public record PaymentCompletedEvent(
    Long paymentId,
    String paymentCode,
    Long bookingId,
    Long userId,
    BigDecimal amount,
    Instant completedAt
) {}
```

### B.4.3. So sánh và quyết định

| Tiêu chí                | Sync (REST)                              | Async (Kafka)                             |
| ----------------------- | ---------------------------------------- | ----------------------------------------- |
| Latency                 | Thấp (real-time)                          | Cao hơn (eventually consistent)           |
| Coupling                | Tight — caller phụ thuộc availability của callee | Loose — producer không cần biết consumer |
| Failure handling        | Caller thấy lỗi ngay → Circuit Breaker    | Retry tự động qua broker                  |
| Use case phù hợp        | Query, validate, lock seat                | Notify, cập nhật trạng thái xuyên service |
| Trade-off               | Cascade failure nếu callee chết           | Khó debug hơn (event eventual)            |

→ **Quyết định:** Sync cho luồng critical-path đặt vé (hold seat), Async cho luồng background (payment confirm → notify, compensation).

## B.5. Pattern cốt lõi — chi tiết triển khai

### B.5.1. Saga (Choreography) — đảm bảo nhất quán Booking ↔ Payment

```mermaid
sequenceDiagram
    actor U as User
    participant BK as booking-service
    participant R as Redis
    participant PM as payment-service
    participant K as Kafka
    participant NT as notification-service

    rect rgb(220, 252, 231)
        Note over U,NT: HAPPY PATH — đặt vé thành công
        U->>BK: POST /bookings/hold
        BK->>R: SETNX seat lock (10min TTL)
        BK->>BK: INSERT booking (HELD)
        BK->>K: publish booking.held
        BK-->>U: bookingId

        U->>PM: POST /payments
        PM->>PM: gateway → SUCCESS
        PM->>K: publish payment.completed

        K->>BK: consume payment.completed
        BK->>BK: UPDATE booking SET status=CONFIRMED
        BK->>R: DEL seat lock
        BK->>K: publish booking.confirmed

        K->>NT: consume booking.confirmed
        NT-->>U: 📧 Email xác nhận
    end

    rect rgb(254, 226, 226)
        Note over U,NT: COMPENSATION — payment fail
        U->>PM: POST /payments
        PM->>PM: gateway → DECLINED
        PM->>K: publish payment.failed

        K->>BK: consume payment.failed
        BK->>BK: UPDATE booking SET status=CANCELLED
        BK->>R: DEL seat lock (release ghế)
        BK->>K: publish booking.cancelled

        K->>NT: consume booking.cancelled
        NT-->>U: 📧 Email báo hủy
    end
```

**Tại sao chọn Choreography thay vì Orchestration?**

- Phù hợp với dự án nhỏ — không có orchestrator (Camunda, Temporal) phức tạp.
- Mỗi service tự biết phải làm gì khi nhận event → loose coupling tối đa.
- Hợp với hành vi thực tế: payment xong → tự nhiên booking confirm, không cần ai chỉ huy.

### B.5.2. Outbox Pattern — đảm bảo at-least-once delivery

**Vấn đề:** Nếu code publish trực tiếp lên Kafka trong transaction:

```java
@Transactional
void hold(...) {
    bookingRepo.save(booking);     // commit DB
    kafkaTemplate.send(event);     // ← nếu Kafka down → event bị mất, DB đã commit!
}
```

**Giải pháp Outbox:**

```mermaid
flowchart LR
    A[booking-service] -->|"@Transactional<br/>1. INSERT booking<br/>2. INSERT outbox_event"| DB[(booking_db)]
    DB -->|polling mỗi 2s| W[Relay Worker]
    W -->|publish & mark SENT| K[Kafka]
    W -.->|nếu fail: retry| W
```

Code:

```java
@Transactional
void hold(...) {
    var booking = bookingRepo.save(...);
    outboxRepo.save(new OutboxEvent(
        "Booking", booking.getId(), "BookingHeldEvent",
        toJson(payload), PENDING
    ));
    // commit cả 2 cùng nhau — atomic
}

@Scheduled(fixedDelay = 2000)
void relay() {
    outboxRepo.findByStatus(PENDING, pageable)
        .forEach(evt -> {
            try {
                kafka.send(evt.getEventType(), evt.getPayload());
                evt.setStatus(SENT);
                evt.setSentAt(now());
            } catch (Exception e) {
                evt.setRetryCount(evt.getRetryCount() + 1);
            }
            outboxRepo.save(evt);
        });
}
```

### B.5.3. Concurrent Booking Resolution — 2 lớp

```mermaid
sequenceDiagram
    actor A as User A
    actor B as User B
    participant BK as booking-service
    participant R as Redis
    participant DB as booking_db

    par Cùng lúc bấm "Đặt ghế 12A"
        A->>BK: POST /hold (seat 12A)
    and
        B->>BK: POST /hold (seat 12A)
    end

    BK->>R: SETNX seat:VN201:12A (User A)
    R-->>BK: OK ✅
    BK->>R: SETNX seat:VN201:12A (User B)
    R-->>BK: NIL ❌

    BK->>DB: INSERT booking (A, HELD)
    Note over A: ✅ HELD thành công<br/>holdExpiresAt = now+10min

    BK-->>B: 409 Conflict<br/>"SEAT_ALREADY_HELD"
    Note over B: ❌ Phải chọn ghế khác

    Note over R: Sau 10 phút<br/>nếu A không pay<br/>Redis tự xóa key (TTL)<br/>+ Scheduler job<br/>UPDATE A.status=EXPIRED
```

### B.5.4. Resilience — Circuit Breaker (Resilience4j)

```mermaid
stateDiagram-v2
    [*] --> CLOSED : khởi tạo
    CLOSED --> OPEN : ≥5 lỗi trong 10 request<br/>(failure rate ≥ 50%)
    OPEN --> HALF_OPEN : sau 30s cooldown
    HALF_OPEN --> CLOSED : 3 request liên tiếp OK
    HALF_OPEN --> OPEN : ≥1 request fail
    OPEN --> [*] : fail fast<br/>(không gọi callee)
```

Cấu hình mẫu cho `booking-service` khi gọi `flight-search-service`:

```yaml
resilience4j:
  circuitbreaker:
    instances:
      flightService:
        failure-rate-threshold: 50
        sliding-window-size: 10
        wait-duration-in-open-state: 30s
        permitted-number-of-calls-in-half-open-state: 3
  timelimiter:
    instances:
      flightService:
        timeout-duration: 3s
  retry:
    instances:
      flightService:
        max-attempts: 3
        wait-duration: 500ms
        exponential-backoff-multiplier: 2
```

### B.5.5. Observability Stack

```
                ┌───────────────────────┐
                │   Microservice (×5)   │
                │  ─────────────────    │
                │  Micrometer           │
                │  + Spring Actuator    │
                └─────┬─────────┬───────┘
                      │         │
        /actuator/    │         │   /actuator/health
        prometheus    │         │   /actuator/info
                      ▼         ▼
              ┌─────────────┐ ┌──────────┐
              │ Prometheus  │ │ Zipkin   │
              │ (metrics)   │ │ (traces) │
              └──────┬──────┘ └──────────┘
                     │
                     ▼
               ┌──────────┐
               │ Grafana  │  ◄── dashboard cho ops
               └──────────┘
```

- **Health:** `/actuator/health` — kiểm tra DB, Redis, Kafka connection.
- **Metrics:** request rate, error rate, p50/p95/p99 latency mỗi endpoint.
- **Tracing:** mỗi request có `traceId`/`spanId` xuyên 5 service → debug 1 lượt booking dễ dàng.
- **Logging:** Logback JSON format kèm `traceId` để LogQL query.

## B.6. API Gateway routing

| Path pattern                | Forward to                                    | Filters                                        |
| --------------------------- | --------------------------------------------- | ---------------------------------------------- |
| `/api/v1/users/**`          | http://user-service:8083                      | RateLimit (10 req/s), no auth cần cho /register, /login |
| `/api/v1/flights/**`        | http://flight-search-service:8081             | RateLimit (50 req/s), public                   |
| `/api/v1/bookings/**`       | http://booking-service:8082                   | JwtAuthFilter, RateLimit (20 req/s)            |
| `/api/v1/payments/**`       | http://payment-service:8084                   | JwtAuthFilter, RateLimit (5 req/s), Idempotency check |

## B.7. Deployment Topology (Docker Compose for dev)

```mermaid
flowchart TB
    subgraph host["🖥️ Docker Host (1 dev machine)"]
        subgraph net["docker network: airline-net"]
            G[api-gateway :8080]
            S1[flight-search :8081]
            S2[booking :8082]
            S3[user :8083]
            S4[payment :8084]
            S5[notification :8085]

            subgraph infra["Shared Infrastructure"]
                PG[(postgres :5432<br/>5 databases)]
                RD[(redis :6379)]
                KF{{kafka :9092}}
                ZP[zipkin :9411]
                GR[grafana :3000]
                MH[mailhog :8025]
            end
        end
    end

    Browser([👤 Browser :8080])
    Browser --> G

    G --> S1 & S2 & S3 & S4 & S5
    S1 & S2 & S3 & S4 & S5 --> PG
    S2 --> RD
    S2 & S4 & S5 --> KF
    S5 --> MH
    S1 & S2 & S3 & S4 & S5 -.tracing.-> ZP
```

## B.8. Tổng kết các quyết định kiến trúc (ADR)

| #  | Quyết định                                                     | Lý do                                                                |
| -- | -------------------------------------------------------------- | -------------------------------------------------------------------- |
| 1  | Microservices (5 service riêng) thay vì monolith               | Phù hợp đề bài, mỗi member có 1 service rõ ràng để focus              |
| 2  | Database-per-Service                                            | Loose coupling ở tầng dữ liệu, dễ scale độc lập                      |
| 3  | API Gateway làm single entrypoint                              | Tập trung auth/rate-limit, ẩn topology service nội bộ                  |
| 4  | Sync REST + Async Kafka (kết hợp)                              | Phù hợp use case: sync cho critical path, async cho event             |
| 5  | Choreography Saga (không orchestrator)                          | Đủ đơn giản cho dự án; loose coupling cao nhất                       |
| 6  | Outbox Pattern                                                  | Đảm bảo at-least-once delivery cho Kafka, không bị mất event          |
| 7  | Redis distributed lock + DB optimistic lock (2 lớp)             | Hiệu năng cao (Redis < 1ms) + an toàn tuyệt đối (DB UNIQUE)           |
| 8  | Resilience4j Circuit Breaker                                    | Tránh cascade failure khi 1 service chết                              |
| 9  | Spring Cloud Gateway (reactive) làm API Gateway                 | Native cho Spring ecosystem, perf cao                                  |
| 10 | Postgres 16 cho tất cả service                                  | Đơn giản hoá ops; vẫn đảm bảo logical separation                     |
| 11 | Flyway migration thay vì Hibernate ddl-auto                     | Schema có lịch sử, deploy production an toàn                          |
| 12 | Distributed tracing với Micrometer + Zipkin                     | Debug 1 request qua 5 service dễ dàng                                  |

---

## Phụ lục — Cách dùng tài liệu này

1. **Phần A** copy vào mục "Database Design" trong file `.docx` template.
2. **Phần B** copy vào mục "System Architecture" trong file `.docx` template.
3. Các Mermaid diagram (block ```mermaid```) có thể:
   - Mở trong VS Code (cài extension "Markdown Preview Mermaid Support") → screenshot → paste vào Word.
   - Hoặc paste code vào https://mermaid.live → export PNG/SVG → chèn Word.
4. Các ERD và sequence diagram đã được vẽ theo notation chuẩn → giảng viên đọc được ngay.
