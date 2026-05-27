# Airline Booking System — Luồng & Kiến trúc

Tất cả sơ đồ vẽ bằng **Mermaid** — VS Code / GitHub / IntelliJ render trực tiếp được.

---

## 1. Sơ đồ tổng quan kiến trúc (Container Diagram)

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
        REDIS[(Redis<br/>seat-hold TTL)]
    end

    subgraph Broker["📨 Message Broker"]
        KAFKA{{Kafka / RabbitMQ}}
    end

    subgraph Obs["📊 Observability"]
        ZIP[Zipkin]
        PROM[Prometheus + Grafana]
    end

    EXT[📧 SMTP / SMS Gateway]

    User --> GW
    GW --> FS
    GW --> BK
    GW --> US
    GW --> PM

    FS --> DB1
    BK --> DB2
    BK --> REDIS
    US --> DB3
    PM --> DB4
    NT --> DB5

    BK -.sync REST.-> FS
    BK -.sync REST.-> US

    BK ==>|publish<br/>BookingHeld<br/>BookingConfirmed| KAFKA
    PM ==>|publish<br/>PaymentCompleted<br/>PaymentFailed| KAFKA
    KAFKA ==>|consume| BK
    KAFKA ==>|consume| NT

    NT --> EXT

    FS & BK & US & PM & NT -.metrics + traces.-> Obs

    classDef svc fill:#dbeafe,stroke:#2563eb,color:#1e3a8a
    classDef db fill:#dcfce7,stroke:#16a34a
    classDef edge fill:#fef3c7,stroke:#d97706
    classDef broker fill:#fce7f3,stroke:#db2777
    class FS,BK,US,PM,NT svc
    class DB1,DB2,DB3,DB4,DB5,REDIS db
    class GW edge
    class KAFKA broker
```

---

## 2. Happy path — Đặt vé thành công (End-to-End)

```mermaid
sequenceDiagram
    autonumber
    actor U as 👤 User
    participant GW as API Gateway
    participant US as user-service
    participant FS as flight-search
    participant BK as booking-service
    participant R as Redis
    participant PM as payment-service
    participant K as Kafka
    participant NT as notification-service

    U->>GW: POST /login (email, password)
    GW->>US: forward
    US-->>U: JWT token

    U->>GW: GET /flights?from=HAN&to=SGN&date=2026-06-30
    GW->>FS: forward
    FS-->>U: [Flight VN201, VN203, ...]

    U->>GW: POST /bookings/hold (flight, seat=12A)
    GW->>BK: forward (with JWT)
    BK->>FS: GET /flights/VN201/seats/12A
    FS-->>BK: available=true, price=1.2M
    BK->>R: SETNX seat:VN201:12A TTL=10min
    R-->>BK: OK (got lock)
    BK->>BK: INSERT booking (status=HELD)
    BK-->>U: bookingId=B001, holdUntil=10min

    U->>GW: POST /payments (bookingId=B001, card=...)
    GW->>PM: forward
    PM->>PM: call mock gateway → SUCCESS
    PM->>PM: INSERT payment (status=SUCCESS)
    PM->>K: publish PaymentCompletedEvent

    K->>BK: consume PaymentCompleted
    BK->>BK: UPDATE booking SET status=CONFIRMED
    BK->>R: DEL seat:VN201:12A
    BK->>K: publish BookingConfirmedEvent

    K->>NT: consume BookingConfirmed
    NT->>NT: render template
    NT-->>U: 📧 Email xác nhận vé
```

---

## 3. Concurrency — 2 user cùng book 1 ghế (Race condition)

Đây là **challenge chính** của đề tài — phải đảm bảo chỉ 1 người thành công.

```mermaid
sequenceDiagram
    autonumber
    actor A as 👤 User A
    actor B as 👤 User B
    participant BK as booking-service
    participant R as Redis
    participant DB as booking_db

    par Cùng lúc
        A->>BK: POST /bookings/hold (seat 12A)
    and
        B->>BK: POST /bookings/hold (seat 12A)
    end

    BK->>R: SETNX seat:VN201:12A (User A)
    R-->>BK: OK ✅
    BK->>R: SETNX seat:VN201:12A (User B)
    R-->>BK: NIL ❌ (đã có lock)

    BK->>DB: INSERT booking (A, status=HELD)
    Note over A: ✅ Hold thành công

    BK-->>B: 409 Conflict<br/>"Ghế đã được giữ"
    Note over B: ❌ Phải chọn ghế khác

    Note over R: Sau 10 phút<br/>nếu A không pay<br/>Redis tự xóa key<br/>(TTL expire)

    alt User A thanh toán kịp
        A->>BK: confirm + payment OK
        BK->>DB: UPDATE booking SET status=CONFIRMED
    else User A bỏ
        Note over R: TTL hết → key xóa
        BK->>DB: UPDATE booking SET status=EXPIRED<br/>(scheduled job)
        Note over B: Lần sau B retry sẽ giữ được
    end
```

**Cơ chế chống double-book 2 lớp:**
1. **Redis SETNX** với TTL — lớp nhanh, in-memory
2. **DB unique constraint** `UNIQUE(flight_id, seat_no) WHERE status IN ('HELD','CONFIRMED')` — lớp backup

---

## 4. Saga — Payment fail → Compensation

Khi payment fail, hệ thống phải tự rollback (release ghế đã giữ).

```mermaid
sequenceDiagram
    autonumber
    actor U as 👤 User
    participant BK as booking-service
    participant R as Redis
    participant PM as payment-service
    participant K as Kafka

    U->>BK: POST /bookings/hold (seat 12A)
    BK->>R: SETNX seat:VN201:12A → OK
    BK->>BK: booking B001 = HELD
    BK->>K: publish BookingHeldEvent
    BK-->>U: bookingId=B001

    U->>PM: POST /payments (B001)
    PM->>PM: call mock gateway → ❌ DECLINED
    PM->>PM: payment.status = FAILED
    PM->>K: publish PaymentFailedEvent

    rect rgb(254, 226, 226)
        Note over K,BK: 🔄 COMPENSATION
        K->>BK: consume PaymentFailed
        BK->>BK: UPDATE booking SET status=CANCELLED
        BK->>R: DEL seat:VN201:12A
        BK->>K: publish BookingCancelledEvent
    end

    PM-->>U: 402 Payment Failed
    Note over U: Có thể retry với thẻ khác<br/>hoặc chọn ghế mới
```

**Choreography Saga** — không có orchestrator trung tâm, mỗi service tự phản ứng theo event.

---

## 5. Resilience — Circuit Breaker khi flight-search die

```mermaid
sequenceDiagram
    autonumber
    actor U as 👤 User
    participant BK as booking-service
    participant CB as Circuit Breaker
    participant FS as flight-search

    rect rgb(220, 252, 231)
        Note over CB: State: CLOSED ✅
        U->>BK: POST /bookings/hold
        BK->>CB: call flight-search
        CB->>FS: GET /flights/.../seats
        FS-->>CB: 200 OK
        CB-->>BK: response
        BK-->>U: ✅ holdId
    end

    Note over FS: 💥 flight-service crash

    rect rgb(254, 243, 199)
        Note over CB: State: CLOSED → HALF_OPEN<br/>(sau N failure)
        U->>BK: POST /bookings/hold
        BK->>CB: call flight-search
        CB->>FS: GET ... → timeout
        CB->>FS: retry → timeout
        Note over CB: 5 lần fail liên tiếp<br/>→ OPEN circuit
    end

    rect rgb(254, 226, 226)
        Note over CB: State: OPEN 🔴<br/>(skip call, fail fast)
        U->>BK: POST /bookings/hold
        BK->>CB: call flight-search
        CB-->>BK: fallback ngay lập tức<br/>(không gọi FS)
        BK-->>U: 503 "Hệ thống bận, thử lại sau"
    end

    Note over FS: ✅ flight-service hồi phục

    rect rgb(254, 243, 199)
        Note over CB: Sau cooldown<br/>State: HALF_OPEN
        U->>BK: POST /bookings/hold
        BK->>CB: call flight-search
        CB->>FS: GET ... → 200 OK
        CB-->>BK: response
        Note over CB: State → CLOSED ✅
    end
```

---

## 6. Data Flow — Event-driven

```mermaid
flowchart LR
    BK[booking-service]
    PM[payment-service]
    NT[notification-service]

    subgraph K[Kafka Topics]
        T1[booking.held]
        T2[booking.confirmed]
        T3[booking.cancelled]
        T4[payment.completed]
        T5[payment.failed]
    end

    BK -->|produce| T1
    BK -->|produce| T2
    BK -->|produce| T3
    PM -->|produce| T4
    PM -->|produce| T5

    T1 -.->|consume| PM
    T4 -.->|consume| BK
    T5 -.->|consume| BK
    T2 -.->|consume| NT
    T3 -.->|consume| NT

    classDef topic fill:#fce7f3,stroke:#db2777
    class T1,T2,T3,T4,T5 topic
```

---

## 7. State Machine — Booking lifecycle

```mermaid
stateDiagram-v2
    [*] --> HELD: POST /hold<br/>+ Redis lock<br/>+ TTL 10min

    HELD --> CONFIRMED: PaymentCompleted<br/>event received
    HELD --> CANCELLED: PaymentFailed<br/>event received
    HELD --> EXPIRED: TTL expired<br/>(scheduled job)

    CONFIRMED --> REFUNDED: user request<br/>refund

    CANCELLED --> [*]
    EXPIRED --> [*]
    CONFIRMED --> [*]
    REFUNDED --> [*]

    note right of HELD
        Seat lock đang active
        Chỉ chờ payment
    end note

    note right of CONFIRMED
        Email đã gửi
        Vé đã issue
    end note
```

---

## 8. Triển khai — Docker Compose topology

```mermaid
flowchart TB
    subgraph host["🖥️ Docker Host"]
        subgraph net["docker network: airline-net"]
            G[api-gateway:8080]
            S1[flight-search:8081]
            S2[booking:8082]
            S3[user:8083]
            S4[payment:8084]
            S5[notification:8085]

            subgraph infra["Infrastructure"]
                PG[(postgres:5432<br/>5 databases)]
                RD[(redis:6379)]
                KF{{kafka:9092}}
                ZK[zookeeper]
                ZP[zipkin:9411]
                GR[grafana:3000]
                MH[mailhog:8025]
            end

            KF --- ZK
        end
    end

    Browser([👤 Browser :8080])
    Browser --> G

    G --> S1 & S2 & S3 & S4 & S5
    S1 & S2 & S3 & S4 & S5 --> PG
    S2 --> RD
    S2 & S4 & S5 --> KF
    S5 --> MH
    S1 & S2 & S3 & S4 & S5 -.-> ZP
```

---

## Tóm tắt các luồng chính

| # | Luồng                      | Service liên quan              | Đặc điểm                                      |
| - | -------------------------- | ------------------------------ | --------------------------------------------- |
| 1 | Đăng ký / Login            | user → gateway                 | Sync, JWT                                     |
| 2 | Tìm chuyến bay             | flight-search                  | Sync, read-heavy → có cache                   |
| 3 | Giữ ghế (hold)             | booking + flight-search        | Sync REST + Redis lock + DB                   |
| 4 | Thanh toán                 | payment                        | Sync vào, async publish event                 |
| 5 | Confirm booking            | booking ← Kafka ← payment      | **Async (Saga)**                              |
| 6 | Gửi email                  | notification ← Kafka ← booking | **Async (event-driven)**                      |
| 7 | Compensation khi fail      | booking ← Kafka ← payment      | **Saga rollback**                             |
| 8 | Race condition 2 user      | booking + Redis                | Concurrency control                           |

→ Tất cả 8 luồng đều đã được vẽ chi tiết ở các diagram phía trên.
