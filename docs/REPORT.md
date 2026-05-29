# GROUP PROJECT DOCUMENTATION

## MICROSERVICES WITH SPRING BOOT

# Airline Booking System

**Prepared by**

**Group Name:** _<điền tên nhóm>_

- _<Họ tên thành viên 1 — MSSV>_
- _<Họ tên thành viên 2 — MSSV>_
- _<Họ tên thành viên 3 — MSSV>_
- _<Họ tên thành viên 4 — MSSV>_
- _<Họ tên thành viên 5 — MSSV>_

**Ho Chi Minh City, 2025**

---

## Table of Contents

1. [Project Introduction](#1-project-introduction)
   - 1.1 [Product Perspective](#11-product-perspective)
   - 1.2 [User Classes and Characteristics](#12-user-classes-and-characteristics)
2. [Database Design](#2-database-design)
3. [System Architecture (Microservices Architecture)](#3-system-architecture-microservices-architecture)
4. [Implementation](#4-implementation)
   - 4.1 [Deployment Considerations](#41-deployment-considerations)
   - 4.2 [Screenshots and explanations](#42-screenshots-and-explanations)
5. [References](#5-references)

---

## Revision History

| Name        | Date        | Reason For Changes                              | Version    |
| ----------- | ----------- | ----------------------------------------------- | ---------- |
| _<Tên>_     | 27 May 2026 | Initial draft                                   | 1.0 draft 1|
| _<Tên>_     | _<dd MMM>_  | Add project introduction & database design       | 1.0 draft 2|
| _<Tên>_     | _<dd MMM>_  | Add system architecture & implementation         | 1.0 draft 3|
| _<Tên>_     | _<dd MMM>_  | Final change for submission                      | 1.0        |

---

# 1. Project Introduction

## 1.1 Product Perspective

**Airline Booking System (ABS)** là hệ thống đặt vé máy bay trực tuyến được xây dựng theo **kiến trúc microservices** với Spring Boot 3.5 và Java 21. Dự án tập trung vào hai bài toán nghiệp vụ khó nhất của ngành hàng không:

1. **Xử lý đặt vé đồng thời (concurrent booking)** — đảm bảo cùng một ghế trên cùng một chuyến bay không thể bị nhiều khách đặt cùng lúc, ngay cả khi hệ thống nhận hàng trăm yêu cầu đồng thời.
2. **Quản lý dữ liệu có tính trạng thái và nhạy cảm thời gian (stateful, time-sensitive data)** — booking ở trạng thái HELD chỉ giữ ghế trong 10 phút, sau đó phải tự động giải phóng nếu khách chưa thanh toán; giá vé biến động theo tỉ lệ ghế đã bán.

Hệ thống được chia thành **5 dịch vụ độc lập**, mỗi service sở hữu database riêng (Database-per-Service pattern) và chỉ giao tiếp với nhau qua REST (đồng bộ) hoặc message broker (bất đồng bộ):

| Service                     | Trách nhiệm                                                                  | Port |
| --------------------------- | ---------------------------------------------------------------------------- | ---- |
| **flight-search-service**   | Quản lý lịch chuyến bay, đường bay, sân bay và số ghế khả dụng              | 8081 |
| **booking-service**         | Quản lý quy trình đặt vé, chọn ghế, giữ chỗ và xác nhận đặt chỗ              | 8082 |
| **user-service**            | Quản lý thông tin hành khách và tài khoản đã đăng ký                          | 8083 |
| **payment-service**         | Xử lý thanh toán cho vé                                                       | 8084 |
| **notification-service**    | Gửi xác nhận đặt vé và nhắc nhở chuyến bay cho hành khách                     | 8085 |
| **api-gateway**             | Cổng vào duy nhất, định tuyến, xác thực JWT, rate-limit                       | 8080 |

**Mục tiêu của dự án:**

- Thực hành thiết kế và triển khai một hệ thống microservice hoàn chỉnh.
- Áp dụng các pattern cốt lõi: **Saga** (đảm bảo tính nhất quán xuyên dịch vụ), **Outbox** (đảm bảo gửi sự kiện đáng tin cậy), **Circuit Breaker** (tự bảo vệ khi một service gặp sự cố), **Optimistic Locking** + **Distributed Lock** (chống tranh chấp ghế).
- Triển khai quan sát hệ thống (Observability) bằng Actuator, Prometheus, Grafana và distributed tracing (Zipkin).

**Phạm vi (Scope):**

| In-scope                                                       | Out-of-scope                                          |
| -------------------------------------------------------------- | ----------------------------------------------------- |
| Tìm chuyến bay, đặt 1 chiều, thanh toán mock, gửi email xác nhận | Đặt vé khứ hồi và chuyến bay nối chuyến (transit)     |
| Đăng ký, đăng nhập (JWT), quản lý hồ sơ                          | Tích hợp cổng thanh toán thật (VNPay, MoMo)            |
| Concurrent booking, refund đơn giản                              | Loyalty / voucher / mileage program                    |
| Observability cơ bản (health, metrics, tracing)                  | Đặt chỗ cho > 1 hành khách trong 1 lượt mua            |

## 1.2 User Classes and Characteristics

### Customer (Hành khách)

| #  | Function Name                        | Function Description                                                                 |
| -- | ------------------------------------ | ------------------------------------------------------------------------------------ |
| 1  | Register account                     | Đăng ký tài khoản mới bằng email + mật khẩu, hệ thống tự khởi tạo hồ sơ hành khách.   |
| 2  | Login / Logout                       | Đăng nhập, nhận JWT để xác thực ở các request sau.                                    |
| 3  | Update profile                       | Cập nhật họ tên, số điện thoại, ngày sinh, số hộ chiếu, quốc tịch.                    |
| 4  | Search flights                       | Tìm chuyến bay theo điểm đi, điểm đến, ngày bay, số hành khách.                       |
| 5  | View flight detail                   | Xem chi tiết chuyến bay, sơ đồ ghế còn trống và giá hiện tại.                          |
| 6  | Hold seat                            | Chọn ghế và giữ chỗ tạm thời trong 10 phút để thanh toán.                              |
| 7  | Make payment                         | Thanh toán cho booking đang giữ — chọn phương thức Card / Wallet / Bank Transfer.     |
| 8  | View booking history                 | Xem danh sách các vé đã đặt, lọc theo trạng thái.                                      |
| 9  | Cancel booking & refund              | Hủy vé đã giữ hoặc đã xác nhận; hệ thống tự hoàn tiền theo chính sách.                |
| 10 | Receive booking confirmation         | Nhận email xác nhận tự động ngay sau khi thanh toán thành công.                       |
| 11 | Receive flight reminder              | Nhận email nhắc nhở trước giờ bay 24 tiếng.                                            |

**Characteristics:** Sử dụng ứng dụng qua trình duyệt web hoặc mobile, không cần kiến thức kỹ thuật. Sẽ truy cập đột biến vào giờ cao điểm (mở bán vé, ngày lễ) — đây là lúc bài toán concurrent booking trở nên gay gắt.

### Admin (Tùy chọn — ngoài MVP)

| #  | Function Name              | Function Description                                                                 |
| -- | -------------------------- | ------------------------------------------------------------------------------------ |
| 1  | Login (admin role)         | Đăng nhập với quyền ADMIN.                                                            |
| 2  | Manage flights (CRUD)       | Thêm / sửa / xóa / tìm chuyến bay, đường bay, sân bay.                                 |
| 3  | View all bookings           | Xem toàn bộ booking trong hệ thống, lọc theo trạng thái, ngày, route.                  |
| 4  | View payment & refund reports | Xem báo cáo doanh thu, các giao dịch hoàn tiền.                                       |

### System Actors (không phải con người)

- **Payment Gateway (mock):** Hệ thống ngoài, trả về SUCCESS / FAILED ngẫu nhiên — phục vụ demo Saga compensation.
- **SMTP / SMS Provider:** MailHog cho môi trường dev, có thể đổi sang Gmail SMTP / Twilio cho production.
- **Scheduler:** Cron job nội bộ — chạy mỗi phút để giải phóng booking HELD đã hết hạn, mỗi giờ để gửi flight reminder.

**Business Rules quan trọng:**

- **BR-01:** Một ghế trên một chuyến bay chỉ được giữ bởi tối đa một booking ở trạng thái HELD hoặc CONFIRMED tại bất kỳ thời điểm nào.
- **BR-02:** Booking ở trạng thái HELD sẽ tự động chuyển sang EXPIRED nếu sau 10 phút khách chưa thanh toán; ghế được giải phóng để khách khác có thể đặt.
- **BR-03:** Nếu thanh toán thất bại, ghế phải được giải phóng ngay lập tức (Saga compensation).
- **BR-04:** Refund 100% nếu hủy trước giờ bay 24 tiếng, refund 50% nếu hủy trong vòng 24 tiếng, không refund sau giờ bay.
- **BR-05:** Giá vé biến động theo tỉ lệ ghế đã bán: `current_price = base_price × (1 + 0.1 × seats_booked / total_seats)`.

---

# 2. Database Design

Hệ thống áp dụng **Database-per-Service pattern**: mỗi microservice sở hữu một database riêng, không có truy vấn xuyên service ở tầng dữ liệu. Tham chiếu giữa các service được lưu dưới dạng **logical foreign key** (chỉ là cột `BIGINT user_id`, `flight_id`, `booking_id` — không có constraint vật lý). DBMS thống nhất: **PostgreSQL 16**.

### 2.1. Tổng quan 5 database

```
┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│   flight_db      │  │    user_db       │  │   booking_db     │
│  ─────────────   │  │  ──────────────  │  │  ──────────────  │
│  airport         │  │  users           │  │  booking         │
│  airline         │  │  passenger       │  │  booking_item    │
│  route           │  │  user_role       │  │  outbox_event    │
│  flight          │  └──────────────────┘  └──────────────────┘
│  seat_inventory  │  ┌──────────────────┐  ┌──────────────────┐
│  flight_seat     │  │   payment_db     │  │   notify_db      │
└──────────────────┘  │  ──────────────  │  │  ──────────────  │
                      │  payment         │  │  notif_template  │
                      │  transaction     │  │  notification    │
                      │  refund          │  │  reminder_log    │
                      │  outbox_event    │  └──────────────────┘
                      └──────────────────┘
```

### 2.2. ERD — flight_db (flight-search-service)

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   AIRPORT   │1──┐ │   AIRLINE   │     │    ROUTE    │
├─────────────┤   │ ├─────────────┤     ├─────────────┤
│ iata_code PK│   ├─│ code     PK │     │ id        PK│
│ name        │   │ │ name        │     │ from_airport│
│ city        │   │ └─────────────┘     │ to_airport  │
│ country     │   │       │1            │ distance_km │
└─────────────┘   │       │              └──────┬──────┘
                  │       │                     │1
                  │       │ ┌────────────────┐  │
                  │       └─│     FLIGHT     │──┘
                  │         ├────────────────┤*
                  │         │ id           PK│
                  └─────────│ flight_no     U│
                            │ route_id    FK │1
                            │ airline_code FK│
                            │ departure_time │
                            │ total_seats    │
                            │ base_price     │
                            │ status         │
                            │ version       OL│
                            └────────┬───────┘
                                     │1
                                     │
                            ┌────────┴───────────┐
                            │  SEAT_INVENTORY    │
                            ├────────────────────┤
                            │ flight_id PK,FK    │
                            │ total / available  │
                            │ held / booked      │
                            │ version          OL│
                            └────────────────────┘
```

**Chú thích:** `PK` = Primary Key · `FK` = Foreign Key · `U` = Unique · `OL` = Optimistic Lock (`@Version`)

### 2.3. ERD — user_db

```
┌─────────────────────────┐         ┌──────────────────────┐
│         USERS           │1───────1│      PASSENGER       │
├─────────────────────────┤         ├──────────────────────┤
│ id                    PK│         │ user_id        PK,FK │
│ email                  U│         │ full_name            │
│ password_hash           │         │ phone                │
│ status (ACTIVE/LOCKED)  │         │ date_of_birth        │
│ created_at              │         │ gender               │
│ last_login_at           │         │ passport_no          │
└──────────┬──────────────┘         │ nationality          │
           │1                       └──────────────────────┘
           │
           │*
┌──────────┴──────────┐
│      USER_ROLE      │
├─────────────────────┤
│ user_id   PK,FK     │
│ role      PK (USER/ADMIN) │
└─────────────────────┘
```

### 2.4. ERD — booking_db (core schema)

```
┌──────────────────────────────────┐         ┌───────────────────────┐
│             BOOKING              │1───────*│     BOOKING_ITEM      │
├──────────────────────────────────┤         ├───────────────────────┤
│ id                            PK │         │ id                  PK│
│ booking_code                   U │         │ booking_id        FK  │
│ user_id  (logical → user_db)     │         │ seat_no               │
│ flight_id (logical → flight_db)  │         │ passenger_name        │
│ status (HELD/CONFIRMED/CANCELLED │         │ passenger_passport    │
│         /EXPIRED/REFUNDED)       │         │ price                 │
│ total_amount / currency          │         └───────────────────────┘
│ held_at / expires_at             │
│ confirmed_at / cancelled_at      │         ┌───────────────────────┐
│ payment_id (logical → payment_db)│         │     OUTBOX_EVENT      │
│ version                       OL │         ├───────────────────────┤
│ created_at                       │         │ id                  PK│
└──────────────────────────────────┘         │ aggregate_type        │
                                             │ aggregate_id          │
                                             │ event_type            │
                                             │ payload         JSONB │
                                             │ status (PENDING/SENT) │
                                             │ retry_count           │
                                             └───────────────────────┘
```

### 2.5. ERD — payment_db

```
┌─────────────────────────────────┐    ┌────────────────────┐
│            PAYMENT              │1──*│   TRANSACTION      │
├─────────────────────────────────┤    ├────────────────────┤
│ id                           PK │    │ id              PK │
│ payment_code                  U │    │ payment_id   FK    │
│ booking_id (logical)            │    │ gateway_txn_id     │
│ user_id (logical)               │    │ gateway_response   │
│ amount / currency               │    │ status             │
│ method (CARD/WALLET/BANK)       │    └────────────────────┘
│ status (PENDING/SUCCESS/FAILED) │
│ idempotency_key             U   │1   ┌────────────────────┐
│ created_at / completed_at       │───*│      REFUND        │
└─────────────────────────────────┘    ├────────────────────┤
                                       │ id              PK │
                                       │ payment_id    FK   │
                                       │ amount / reason    │
                                       │ status             │
                                       └────────────────────┘
```

### 2.6. ERD — notify_db

```
┌──────────────────────────────┐        ┌──────────────────────────┐
│    NOTIFICATION_TEMPLATE     │        │      NOTIFICATION        │
├──────────────────────────────┤        ├──────────────────────────┤
│ code      PK (composite)     │        │ id                    PK │
│ locale    PK                 │ template│ template_code         FK │
│ channel   PK                 │◄───────│ user_id (logical)        │
│ subject                      │   reuse │ channel (EMAIL/SMS/PUSH)│
│ body  (Mustache template)    │        │ recipient                │
└──────────────────────────────┘        │ variables          JSONB │
                                        │ status                   │
                                        │ retry_count / error_msg  │
                                        │ created_at / sent_at     │
                                        └──────────────────────────┘

┌──────────────────────────────────┐
│      FLIGHT_REMINDER_LOG         │   (chống gửi reminder trùng)
├──────────────────────────────────┤
│ booking_id  PK                   │
│ flight_id                        │
│ sent_at                          │
└──────────────────────────────────┘
```

### 2.7. Các quyết định thiết kế DB quan trọng

| Quyết định                                                   | Lý do                                                                         |
| ------------------------------------------------------------ | ----------------------------------------------------------------------------- |
| `@Version` trên Flight, SeatInventory, Booking               | Optimistic locking — chống mất cập nhật khi nhiều request đồng thời.           |
| Partial index `WHERE status = 'HELD'` trên `booking.expires_at` | Job quét expired booking chạy nhanh, index nhỏ hơn rất nhiều.                |
| `outbox_event` ở booking + payment                            | Outbox pattern — đảm bảo at-least-once delivery của Kafka event.              |
| `idempotency_key` unique trên `payment`                       | Chống double-submit khi user bấm nút Pay nhiều lần.                            |
| `CHECK` constraint cho mọi trường enum                        | Type safety ở tầng DB — tránh "magic string" gây corrupt data.                |
| Logical FK xuyên service (không phải FK vật lý)               | Tuân thủ Database-per-Service — services không bị coupled bằng schema chung.  |
| 1 Postgres container chứa 5 logical database                  | Tiết kiệm tài nguyên cho máy dev, vẫn đảm bảo cô lập logic mỗi service.       |

---

# 3. System Architecture (Microservices Architecture)

## 3.1. Hardware & Environment

| Item               | Specification                                                                |
| ------------------ | ---------------------------------------------------------------------------- |
| Development OS     | Windows 11 / macOS 14 / Ubuntu 22.04                                         |
| Runtime            | Java 21 (Eclipse Temurin), Docker Desktop 4.30+                              |
| Containerization   | Docker Compose v2 cho môi trường dev local                                    |
| Database           | PostgreSQL 16 (Alpine image)                                                  |
| Cache & Lock       | Redis 7 (Alpine image)                                                        |
| Message Broker     | Apache Kafka 3.7 (KRaft mode, không cần Zookeeper)                            |
| Observability      | Zipkin 3 (tracing), Prometheus + Grafana (metrics), MailHog (email dev)       |
| Build & Dependency | Maven 3.9 (qua Maven Wrapper `mvnw`)                                          |
| User location      | Toàn bộ developer và end user tại Việt Nam — múi giờ UTC+7. Không yêu cầu CDN đa khu vực ở phase MVP. |

## 3.2. Architecture Style

Dự án áp dụng **Microservices Architecture** với các đặc trưng:

- **Database-per-Service** — không service nào chia sẻ DB.
- **API Gateway** — cổng vào duy nhất, định tuyến và bảo mật.
- **Synchronous + Asynchronous communication** — REST cho query, Kafka cho event.
- **Choreography-based Saga** — không có orchestrator trung tâm cho luồng booking ↔ payment.
- **Outbox Pattern** — đảm bảo gửi event đáng tin cậy.
- **Clean / Hexagonal Architecture** trong mỗi service (api / application / domain / infrastructure).

## 3.3. Container Diagram

```
                       ┌─────────────────────┐
                       │   Browser / Mobile  │
                       └──────────┬──────────┘
                                  │ HTTPS / JWT
                                  ▼
                       ┌─────────────────────┐
                       │     API GATEWAY     │  ◄── routing, auth, rate-limit
                       │       :8080         │
                       └──────────┬──────────┘
                                  │
        ┌─────────────┬───────────┼────────────┬─────────────┐
        │             │           │            │             │
        ▼             ▼           ▼            ▼             ▼
┌──────────────┐ ┌──────────┐ ┌─────────┐ ┌──────────┐ ┌──────────────┐
│flight-search │ │ booking  │ │  user   │ │ payment  │ │ notification │
│   :8081      │ │  :8082   │ │  :8083  │ │  :8084   │ │   :8085      │
└──────┬───────┘ └────┬─────┘ └────┬────┘ └────┬─────┘ └──────┬───────┘
       │              │ ▲          │           │              │
       │       sync   │ │  Redis   │           │              │
       │       REST   │ │ (seat-   │           │              │
       │◄─────────────┘ │  hold)   │           │              │
       │                │          │           │              │
       ▼                ▼          ▼           ▼              ▼
┌──────────────┐ ┌────────────┐ ┌───────┐ ┌──────────┐ ┌────────────┐
│  flight_db   │ │ booking_db │ │user_db│ │payment_db│ │ notify_db  │
└──────────────┘ └────────────┘ └───────┘ └──────────┘ └────────────┘

           ┌────────────────────────────────────────────┐
           │              Apache Kafka                  │
           │  topics: booking.held, booking.confirmed,  │
           │  payment.completed, payment.failed, ...    │
           └────────────────────────────────────────────┘
                ▲ produce         ▲ produce
                │                 │
            booking-svc        payment-svc
                │                 │
                ▼ consume         ▼ consume
            payment-svc         booking-svc
                                 notification-svc
```

## 3.4. Communication Patterns

### Synchronous (REST over HTTP)

Dùng khi caller cần phản hồi ngay lập tức:

- `booking-service` → `flight-search-service`: kiểm tra ghế còn trống trước khi giữ.
- `booking-service` → `user-service`: validate hành khách tồn tại.
- Mọi request từ client → các service đều đi qua API Gateway.

Thư viện: `RestTemplate` (đang dùng) → có thể nâng cấp sang `OpenFeign` để gọi service-to-service dễ đọc hơn.

### Asynchronous (Apache Kafka)

Dùng cho event-driven flow để giảm coupling và tăng resilience:

| Topic                  | Producer       | Consumer                  | Payload chính                |
| ---------------------- | -------------- | ------------------------- | ---------------------------- |
| `booking.held`         | booking-svc    | payment-svc (chờ pay)     | bookingId, userId, amount    |
| `booking.confirmed`    | booking-svc    | notification-svc          | bookingId, email, flight info|
| `booking.cancelled`    | booking-svc    | notification-svc          | bookingId, reason            |
| `payment.completed`    | payment-svc    | booking-svc               | bookingId, paymentId         |
| `payment.failed`       | payment-svc    | booking-svc (compensation)| bookingId, reason            |

## 3.5. Key Patterns chi tiết

### 3.5.1. Concurrent Seat Booking — 2 lớp chống tranh chấp

```
Lớp 1 — Redis distributed lock:
   SETNX seat:{flightId}:{seatNo}  TTL=10 phút
   → Atomic, in-memory, < 1ms latency
   → Chỉ 1 request thắng, các request khác → 409 Conflict ngay

Lớp 2 — Database optimistic locking:
   UPDATE booking SET ... WHERE id=? AND version=?
   → Nếu version mismatch → throw OptimisticLockException
   → Backup nếu Redis fail hoặc bị flush
```

### 3.5.2. Saga (Choreography) cho luồng Booking ↔ Payment

```
Bước  Action                                               Service        State change
─────────────────────────────────────────────────────────────────────────────────────
 1    User hold seat                                        booking-svc    booking=HELD
 2    booking-svc publish "booking.held"                    booking-svc    outbox=SENT
 3    User submit payment                                    payment-svc    payment=PENDING
 4a   Gateway returns SUCCESS                                payment-svc    payment=SUCCESS
       → publish "payment.completed"
 5a   booking-svc consume                                    booking-svc    booking=CONFIRMED
       → release Redis lock, publish "booking.confirmed"
 6a   notification-svc consume                               notif-svc      send email ✉

 ── COMPENSATION (rollback) ────────────────────────────────────────────────────────
 4b   Gateway returns FAILED                                 payment-svc    payment=FAILED
       → publish "payment.failed"
 5b   booking-svc consume                                    booking-svc    booking=CANCELLED
       → release Redis lock, publish "booking.cancelled"
 6b   notification-svc consume                               notif-svc      send fail email
```

### 3.5.3. Outbox Pattern (đảm bảo at-least-once delivery)

Thay vì publish trực tiếp lên Kafka trong transaction nghiệp vụ (có nguy cơ DB commit nhưng Kafka send fail), ta:

1. INSERT record vào bảng `outbox_event` (cùng transaction với business write) — atomic.
2. Một relay worker đọc `outbox_event WHERE status = PENDING` mỗi vài giây, publish lên Kafka, đánh dấu SENT.
3. Nếu publish fail → giữ PENDING, tăng `retry_count` → lần sau retry tiếp.

### 3.5.4. Resilience — Circuit Breaker (Resilience4j)

Bọc mọi cuộc gọi sync giữa services:

```java
@CircuitBreaker(name = "flightService", fallbackMethod = "fallbackCheckSeat")
public SeatAvailability checkSeat(Long flightId, String seatNo) { ... }
```

Trạng thái: **CLOSED → OPEN → HALF_OPEN → CLOSED** tự động dựa trên tỉ lệ lỗi và thời gian cooldown. Khi OPEN, request fail nhanh (fail-fast), không chờ timeout → tránh cascade failure.

### 3.5.5. Observability

- **Health check:** mỗi service expose `/actuator/health` (kiểm tra DB, Redis, Kafka).
- **Metrics:** `/actuator/prometheus` → scrape bởi Prometheus → vẽ dashboard Grafana.
- **Distributed tracing:** Spring Boot 3.x dùng Micrometer Tracing, gửi span lên Zipkin → trace 1 request qua 5 service.
- **Structured logging:** Logback JSON với `traceId` và `spanId` trong mỗi log line.

## 3.6. New Technologies (so với chương trình học)

| Technology               | Mục đích trong dự án                                  | Tài liệu chính                            |
| ------------------------ | ----------------------------------------------------- | ----------------------------------------- |
| **Spring Cloud Gateway** | API Gateway phản ứng (reactive), routing động         | Spring Cloud 2025.0.0 docs                |
| **Apache Kafka**         | Message broker cho async event                        | kafka.apache.org/documentation            |
| **Resilience4j**         | Circuit Breaker, Retry, Bulkhead                      | resilience4j.readme.io                    |
| **Flyway**               | Database migration được version hóa                    | flywaydb.org/documentation                |
| **Redis (distributed lock)** | Khóa ghế phân tán cho concurrent booking           | redis.io/docs/manual/patterns/distributed-locks |
| **Outbox Pattern**       | Tích hợp transactional với Kafka                       | microservices.io/patterns/data/transactional-outbox.html |
| **Choreography Saga**    | Đảm bảo nhất quán xuyên dịch vụ không có orchestrator | microservices.io/patterns/data/saga.html  |
| **Micrometer Tracing + Zipkin** | Distributed tracing trong Spring Boot 3        | micrometer.io/docs/tracing                |
| **Testcontainers**       | Tích hợp test với Postgres + Kafka thật trong container| testcontainers.com                        |

---

# 4. Implementation

## 4.1. Deployment Considerations

### 4.1.1. Người dùng và quyền truy cập

- **Customer** truy cập qua trình duyệt từ Việt Nam, tập trung quanh giờ cao điểm (08:00–22:00). Yêu cầu uptime ≥ 99% trong khung này.
- **Admin** (nếu triển khai) truy cập qua đường nội bộ, được giới hạn IP allowlist.
- **Developer / DevOps:** truy cập SSH vào server staging / production, kiểm soát qua bastion host.

### 4.1.2. Hạ tầng

**Môi trường Development (local):**

- 1 Docker Compose file chạy 5 service + Postgres + Redis + Kafka + Zipkin + MailHog + pgAdmin trên 1 máy dev.
- Yêu cầu: 8 GB RAM, 4 core CPU, 20 GB ổ cứng.

**Môi trường Staging / Production:**

| Thành phần        | Triển khai                                                                   |
| ----------------- | ----------------------------------------------------------------------------- |
| Microservices     | Docker container chạy trên Kubernetes (k3s / EKS / GKE). Mỗi service 2 replica.|
| PostgreSQL        | Managed service: AWS RDS, Google Cloud SQL hoặc VPS chuyên dụng. Backup hằng ngày. |
| Redis             | Managed Redis (Redis Cloud, ElastiCache) hoặc tự host. Persistence AOF.       |
| Kafka             | Managed (Confluent Cloud, AWS MSK) hoặc tự host 3-broker.                     |
| Object storage    | S3-compatible cho log archive và database backup.                              |
| CDN               | Cloudflare hoặc AWS CloudFront cho static asset của frontend.                  |
| Email / SMS       | SendGrid / Amazon SES cho email; Twilio cho SMS (production).                  |

### 4.1.3. CI / CD

- **GitHub Actions** trigger trên mỗi push lên `main`:
  1. `mvn clean verify` — chạy test cho tất cả module.
  2. Build Docker image cho mỗi service → push lên container registry.
  3. Deploy lên staging tự động; production cần manual approval.
- **Database migration** chạy qua Flyway tự động khi service khởi động.

### 4.1.4. Capacity Planning

- Mỗi service: 512 MB RAM, 0.5 CPU, scale ngang khi CPU > 70% trong 5 phút.
- Postgres: dự kiến 50 GB dữ liệu sau 1 năm, dùng connection pool 20 / service.
- Kafka: retention 7 ngày cho event; partition theo `bookingId` để đảm bảo thứ tự xử lý.

### 4.1.5. Training & Hand-over

- **Developer onboarding:** đọc `README.md`, `docs/architecture.md`, `docs/design.md` (kèm file này) → chạy `docker compose up` → trong 30 phút có thể chạy thử end-to-end.
- **Business / QA:** swagger UI tại `/swagger-ui.html` của mỗi service liệt kê đầy đủ API → có thể test thủ công không cần code.
- **Operations:** Grafana dashboard có sẵn cho 5 service, alert khi error rate > 1% hoặc latency p99 > 1s.

### 4.1.6. Monitoring & Alerting

| Metric                    | Threshold cảnh báo            | Hành động                                 |
| ------------------------- | ----------------------------- | ----------------------------------------- |
| Service down              | Health check fail 2 lần liên tiếp | PagerDuty → on-call engineer             |
| Error rate > 1%           | trong 5 phút                  | Slack alert                                |
| Latency p99 > 1000 ms     | trong 5 phút                  | Slack alert                                |
| Kafka consumer lag > 10000 | trong 10 phút                 | Slack alert → kiểm tra consumer           |
| Postgres connection > 80% | trong 5 phút                  | Slack alert → tăng pool size               |

## 4.2. Screenshots and explanations

> _Phần này điền sau khi đã có UI và demo end-to-end. Dưới đây là khung sẵn cho từng luồng — chỉ cần chèn screenshot tương ứng._

### 4.2.1. Screen flow

```
   ┌─────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
   │ Login / │───►│  Search  │───►│  Flight  │───►│  Select  │───►│  Payment │
   │ Register│    │  Flight  │    │   List   │    │   Seat   │    │   Form   │
   └─────────┘    └──────────┘    └──────────┘    └──────────┘    └────┬─────┘
                                                                       │
                                                                       ▼
                                                                  ┌──────────┐
                                                                  │ Booking  │
                                                                  │ Confirm  │
                                                                  └────┬─────┘
                                                                       │
                                                                       ▼
                                                                  ┌──────────┐
                                                                  │  Email   │
                                                                  │   Sent   │
                                                                  └──────────┘
```

### 4.2.2. Screenshots checklist

| # | Screenshot                              | Giải thích                                                                |
| - | --------------------------------------- | ------------------------------------------------------------------------- |
| 1 | _<Trang Login>_                         | Form đăng nhập, validate email + password.                                 |
| 2 | _<Trang Register>_                      | Form đăng ký, hiển thị độ mạnh password.                                   |
| 3 | _<Trang Search Flight>_                 | Form chọn điểm đi/đến, ngày, số khách.                                     |
| 4 | _<Danh sách kết quả tìm kiếm>_           | Hiển thị các chuyến bay khớp điều kiện, có giá hiện tại đã tính động.      |
| 5 | _<Trang chi tiết chuyến bay + sơ đồ ghế>_| Sơ đồ ghế, ghế xám = đã đặt, xanh = trống, đỏ = đang chọn.                  |
| 6 | _<Form thanh toán>_                     | Chọn phương thức, nhập thông tin, hiển thị giá tổng.                       |
| 7 | _<Trang xác nhận booking thành công>_    | Mã booking + thông báo sẽ nhận email.                                      |
| 8 | _<Email xác nhận (MailHog)>_            | Nội dung email render từ template `BOOKING_CONFIRMED`.                     |
| 9 | _<Dashboard Grafana>_                   | Metrics: request rate, error rate, latency p99 của 5 service.              |
| 10| _<Zipkin trace 1 request>_              | Trace 1 lượt booking đi qua API Gateway → booking → flight-search.         |
| 11| _<Demo Concurrent Booking>_             | Hai user cùng đặt 1 ghế — 1 thành công, 1 nhận 409 Conflict.               |
| 12| _<Demo Saga compensation>_              | Payment fail → booking auto chuyển CANCELLED → ghế được trả về.            |

### 4.2.3. API Demo (Postman)

| Use case            | Endpoint                                              | Method |
| ------------------- | ----------------------------------------------------- | ------ |
| Register            | `/api/v1/users/register`                              | POST   |
| Login               | `/api/v1/users/login`                                 | POST   |
| Search flights      | `/api/v1/flights?from=HAN&to=SGN&date=2026-06-30`      | GET    |
| Hold seat           | `/api/v1/bookings/hold`                                | POST   |
| Confirm payment     | `/api/v1/payments`                                     | POST   |
| View my bookings    | `/api/v1/bookings/me`                                  | GET    |
| Cancel booking      | `/api/v1/bookings/{id}`                                | DELETE |

---

# 5. References

1. **Spring Boot 3.5 Reference Documentation** — https://docs.spring.io/spring-boot/3.5/reference/
2. **Spring Cloud 2025.0.0 Reference** — https://docs.spring.io/spring-cloud/docs/2025.0.0/reference/html/
3. **Microservices.io — Patterns** — Chris Richardson, https://microservices.io/patterns/
   - Database-per-Service · Saga · Transactional Outbox · API Gateway
4. **Apache Kafka Documentation** — https://kafka.apache.org/documentation/
5. **Resilience4j User Guide** — https://resilience4j.readme.io/docs
6. **Flyway Database Migration** — https://flywaydb.org/documentation/
7. **PostgreSQL 16 Documentation** — https://www.postgresql.org/docs/16/
8. **Redis Distributed Locks (Redlock)** — https://redis.io/docs/manual/patterns/distributed-locks/
9. **Testcontainers for Java** — https://java.testcontainers.org/
10. **Micrometer Tracing** — https://micrometer.io/docs/tracing
11. **Building Microservices, 2nd Edition** — Sam Newman, O'Reilly Media, 2021.
12. **Microservices Patterns** — Chris Richardson, Manning Publications, 2018.
