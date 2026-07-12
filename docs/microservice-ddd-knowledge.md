# Kiến thức tổng hợp: Microservices + DDD

> Tài liệu này dành cho người mới bắt đầu làm microservices.
> Mọi khái niệm được giải thích **bằng tiếng Việt**, kèm ví dụ cụ thể mapping vào dự án **Airline Booking System**.

---

## Mục lục

1. [Microservices là gì?](#1-microservices-là-gì)
2. [DDD (Domain-Driven Design) là gì?](#2-ddd-domain-driven-design-là-gì)
3. [Các tầng trong DDD — giải thích từng package](#3-các-tầng-trong-ddd--giải-thích-từng-package)
4. [Giải thích chi tiết từng file/folder trong booking-service](#4-giải-thích-chi-tiết-từng-filefolder-trong-booking-service)
5. [Các Design Pattern sử dụng](#5-các-design-pattern-sử-dụng)
6. [Giao tiếp giữa các services](#6-giao-tiếp-giữa-các-services)
7. [Công nghệ sử dụng — giải thích từng cái](#7-công-nghệ-sử-dụng--giải-thích-từng-cái)
8. [Luồng hoạt động end-to-end](#8-luồng-hoạt-động-end-to-end)
9. [Thuật ngữ thường gặp](#9-thuật-ngữ-thường-gặp)

---

## 1. Microservices là gì?

### Kiến trúc Monolithic (truyền thống)

Tưởng tượng bạn xây 1 tòa nhà chung cư lớn — **tất cả mọi thứ nằm trong 1 khối**:

```
┌──────────────────────────────────────────┐
│         MỘT ỨNG DỤNG DUY NHẤT           │
│                                          │
│  Tìm chuyến bay + Đặt vé + Thanh toán   │
│  + Quản lý user + Gửi email             │
│                                          │
│         → 1 database duy nhất            │
│         → 1 codebase duy nhất            │
│         → deploy cả khối cùng lúc        │
└──────────────────────────────────────────┘
```

**Vấn đề:** Sửa 1 chỗ → phải deploy lại toàn bộ. 1 phần crash → toàn bộ crash.

### Kiến trúc Microservices

Thay vì 1 khối, chia thành **nhiều service nhỏ, độc lập**:

```
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│ flight-search│  │   booking    │  │    user      │
│   service    │  │   service    │  │   service    │
│  :8081       │  │  :8082 ⭐    │  │  :8083       │
│  flight_db   │  │  booking_db  │  │  user_db     │
└──────────────┘  └──────────────┘  └──────────────┘
┌──────────────┐  ┌──────────────┐
│   payment    │  │ notification │
│   service    │  │   service    │
│  :8084       │  │  :8085       │
│  payment_db  │  │  notify_db   │
└──────────────┘  └──────────────┘
```

### 5 đặc điểm quan trọng của Microservices

| Đặc điểm | Giải thích | Ví dụ trong dự án |
|-----------|-----------|-------------------|
| **Độc lập deploy** | Sửa booking-service → chỉ deploy booking, không ảnh hưởng services khác | Bạn sửa code booking → chỉ restart `localhost:8082` |
| **Database riêng** | Mỗi service có database riêng, KHÔNG share | `booking_db`, `flight_db`, `user_db`... mỗi cái riêng biệt |
| **Giao tiếp qua API** | Services nói chuyện qua HTTP REST hoặc message broker, KHÔNG gọi trực tiếp DB nhau | Booking gọi `GET http://localhost:8081/api/v1/flights/...` |
| **Tự chủ công nghệ** | Mỗi service có thể dùng tech khác nhau (thực tế dự án này dùng chung Java) | Tất cả dùng Spring Boot, nhưng có thể dùng Node.js nếu muốn |
| **Bounded Context** | Mỗi service sở hữu 1 phạm vi nghiệp vụ rõ ràng | Booking chỉ biết về đặt vé, không biết cách hash password |

### Tại sao cần API Gateway?

User không gọi trực tiếp 5 services. Thay vào đó, có 1 **cổng duy nhất** (API Gateway `:8080`):

```
User ──→ API Gateway (:8080) ──→ routing đến service phù hợp
                │
                ├── /api/v1/flights/**  → flight-search (:8081)
                ├── /api/v1/bookings/** → booking (:8082)
                ├── /api/v1/users/**    → user (:8083)
                └── /api/v1/payments/** → payment (:8084)
```

Gateway làm thêm: kiểm tra JWT token, rate limiting (chống spam request), CORS.

---

## 2. DDD (Domain-Driven Design) là gì?

### Tóm gọn 1 câu

> **DDD = Thiết kế phần mềm theo nghiệp vụ thực tế, không theo database.**

### So sánh cách tư duy

| Cách truyền thống (Data-Driven) | Cách DDD (Domain-Driven) |
|---------------------------------|--------------------------|
| "Mình có bảng `booking`, `booking_item` → viết CRUD cho chúng" | "Trong thế giới thật, 1 booking có trạng thái, có luật chuyển đổi trạng thái, có items → code phải phản ánh điều đó" |
| Business logic nằm rải rác trong Service, Controller | Business logic tập trung trong **Domain objects** |
| Entity = mapping 1:1 với bảng DB | **Aggregate** = nhóm objects liên quan, có business rules |

### Các khái niệm DDD cốt lõi

#### 🧠 Aggregate Root (Gốc tổng hợp)

**Là gì:** Đối tượng **trung tâm** quản lý 1 nhóm objects liên quan. Mọi thay đổi phải đi qua nó.

**Ví dụ:** `BookingAggregate` là aggregate root. Muốn thêm `BookingItem`, phải gọi `booking.addItem(item)` — không được tạo item lẻ rồi tự gán.

```java
// ✅ Đúng DDD — thay đổi qua aggregate root
booking.confirm("PAY-001");     // aggregate tự validate + thay đổi status

// ❌ Sai DDD — thay đổi trực tiếp field
booking.setStatus(CONFIRMED);   // ai cũng set được, không validate gì cả
```

**Tại sao:** Đảm bảo **business rules luôn được kiểm tra**. Ví dụ: không thể confirm booking đang ở trạng thái CANCELLED.

#### 📦 Entity (Thực thể)

**Là gì:** Object có **identity** (ID riêng). Hai entity có cùng data nhưng khác ID → chúng là 2 thứ khác nhau.

**Ví dụ:**
- `BookingAggregate` — có `id`, `bookingCode` → 2 booking khác id = 2 booking khác nhau
- `BookingItem` — có `id` riêng, thuộc về 1 booking

#### 💎 Value Object (Đối tượng giá trị)

**Là gì:** Object **không có identity**, chỉ quan trọng **giá trị** của nó. Immutable (không thay đổi được).

**Ví dụ:**
- `BookingStatus` (enum: HELD, CONFIRMED, CANCELLED...) — bạn không care "cái HELD này có id là bao nhiêu", bạn chỉ care nó là "HELD"
- `Money(1500000, "VND")` — hai cái Money cùng giá trị = giống nhau

#### 🔄 Domain Event (Sự kiện miền)

**Là gì:** Mô tả **"điều gì đã xảy ra"** trong domain. Dùng thì quá khứ.

**Ví dụ:**
- `BookingHeldEvent` — "Một booking vừa được giữ chỗ"
- `BookingConfirmedEvent` — "Một booking vừa được xác nhận"
- `PaymentCompletedEvent` — "Thanh toán vừa hoàn tất"

**Dùng để:** Thông báo cho các service khác mà **không cần biết ai đang lắng nghe**. Booking-service chỉ "hét lên" rằng `BookingConfirmed`, notification-service tự nghe và gửi email.

#### 🗄️ Repository (Kho lưu trữ)

**Là gì:** Interface (cổng) để lưu/lấy aggregate từ database. **Domain layer chỉ biết interface**, không biết dùng PostgreSQL hay MongoDB.

```java
// Ở domain layer — chỉ là interface, không biết gì về JPA
public interface BookingRepository {
    BookingAggregate save(BookingAggregate aggregate);
    Optional<BookingAggregate> findById(Long id);
}

// Ở infrastructure layer — implementation thật, dùng JPA
@Component
public class BookingRepositoryAdapter implements BookingRepository {
    private final BookingJpaRepository jpaRepo; // Spring Data JPA
    // ... map entity ↔ aggregate rồi gọi jpaRepo
}
```

**Tại sao:** Nếu mai mốt đổi từ PostgreSQL sang MongoDB, chỉ cần viết lại `BookingRepositoryAdapter`, không cần sửa domain hay service.

---

## 3. Các tầng trong DDD — giải thích từng package

### Sơ đồ các tầng

```
╔══════════════════════════════════════════════════════════════╗
║                    🌐 API LAYER (api/)                      ║
║  Nhận request HTTP từ bên ngoài, trả response              ║
║  → BookingController                                        ║
╠══════════════════════════════════════════════════════════════╣
║                 📋 APPLICATION LAYER (application/)          ║
║  Điều phối luồng xử lý (use case). KHÔNG chứa business     ║
║  logic — chỉ gọi domain objects + infrastructure            ║
║  → BookingService, BookingEventPublisher                    ║
║  → dto/ (HoldSeatRequest, HoldSeatResponse...)              ║
╠══════════════════════════════════════════════════════════════╣
║              🧠 DOMAIN LAYER (domain/) — TRÁI TIM           ║
║  Chứa business rules, KHÔNG phụ thuộc framework nào        ║
║  → aggregate/ (BookingAggregate, BookingItem)                ║
║  → vo/ (BookingStatus)                                      ║
║  → repository/ (BookingRepository interface)                ║
║  → event/ (BookingHeldEvent...)                             ║
║  → exception/ (SeatAlreadyHeldException...)                 ║
╠══════════════════════════════════════════════════════════════╣
║           🔧 INFRASTRUCTURE LAYER (infrastructure/)          ║
║  Chi tiết kỹ thuật: database, Redis, Kafka, REST calls      ║
║  → persistence/ (JPA entities, repositories)                ║
║  → messaging/ (Kafka producer/consumer)                     ║
║  → adapter/ (BookingRepositoryAdapter)                      ║
║  → client/ (FlightSearchClient, UserServiceClient)          ║
╚══════════════════════════════════════════════════════════════╝
```

### Quy tắc dependency (ai được gọi ai)

```
API → Application → Domain ← Infrastructure
                      ↑           ↑
                      └───────────┘
                    Infrastructure implements
                    domain interfaces
```

**Quy tắc vàng:**
- ✅ Controller → gọi Service → gọi Domain objects
- ✅ Infrastructure → implements Domain interfaces
- ❌ Domain KHÔNG ĐƯỢC import bất kỳ thứ gì từ Spring, JPA, Kafka, Redis
- ❌ Application KHÔNG ĐƯỢC import JPA Entity trực tiếp

### Tại sao tách tầng?

| Không tách tầng | Có tách tầng DDD |
|-----------------|------------------|
| Controller gọi thẳng JPA Repository | Controller → Service → Domain Repository (interface) → Adapter → JPA |
| Business logic nằm trong Controller hoặc Service | Business logic nằm trong Aggregate Root |
| Đổi database → sửa khắp nơi | Đổi database → chỉ sửa Infrastructure layer |
| Test khó — phải mock database | Test dễ — domain layer thuần Java, không cần mock |

---

## 4. Giải thích chi tiết từng file/folder trong booking-service

### 📁 Cấu trúc tổng thể

```
booking-service/
├── pom.xml                          ← Cấu hình Maven (dependencies)
├── src/main/java/com/abs/booking/
│   ├── BookingServiceApplication.java
│   ├── api/
│   ├── application/
│   ├── config/
│   ├── domain/
│   └── infrastructure/
└── src/main/resources/
    ├── application.yml              ← Cấu hình Spring Boot
    └── db/migration/
        └── V1__init_schema.sql      ← Script tạo bảng DB
```

---

### 📄 `BookingServiceApplication.java` — Điểm khởi động

```java
@SpringBootApplication   // ← Đánh dấu đây là ứng dụng Spring Boot
@EnableScheduling         // ← Bật tính năng @Scheduled (chạy job định kỳ)
public class BookingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookingServiceApplication.class, args);
        // Khi chạy, Spring Boot sẽ:
        // 1. Scan tất cả @Component, @Service, @Repository, @Controller
        // 2. Tạo beans và inject dependencies
        // 3. Khởi động web server trên port 8082
        // 4. Kết nối PostgreSQL, Redis, Kafka
    }
}
```

**`@EnableScheduling`** cần cho: job `releaseExpiredHolds()` chạy mỗi 60 giây để xóa booking hết hạn.

---

### 📁 `api/` — API Layer (Tầng giao diện REST)

> **Nhiệm vụ:** Nhận HTTP request → gọi Application Service → trả HTTP response.

#### `BookingController.java`

```java
@RestController                        // ← Đánh dấu class này xử lý REST API
@RequestMapping("/bookings")           // ← Base path: /bookings
@RequiredArgsConstructor               // ← Lombok tự tạo constructor inject
public class BookingController {

    private final BookingService bookingService;   // ← Inject application service

    @PostMapping("/hold")              // ← POST /api/v1/bookings/hold
    public ResponseEntity<HoldSeatResponse> holdSeat(
            @Valid @RequestBody HoldSeatRequest request,     // ← Parse JSON body
            @RequestHeader("X-User-Id") Long userId) {       // ← Lấy userId từ header
        // Controller CHỈ làm: nhận request → gọi service → trả response
        // KHÔNG có business logic ở đây
        HoldSeatResponse response = bookingService.holdSeat(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")               // ← GET /api/v1/bookings/123
    @GetMapping("/me")                 // ← GET /api/v1/bookings/me (my bookings)
    @DeleteMapping("/{id}")            // ← DELETE /api/v1/bookings/123 (cancel)
}
```

**Annotation giải thích:**

| Annotation | Nghĩa là gì |
|-----------|-------------|
| `@RestController` | Class này xử lý REST API, tự động serialize/deserialize JSON |
| `@RequestMapping("/bookings")` | Tất cả endpoint trong class bắt đầu bằng `/bookings` |
| `@PostMapping("/hold")` | Xử lý HTTP POST request tới `/bookings/hold` |
| `@GetMapping("/{id}")` | Xử lý HTTP GET, `{id}` là path variable (ví dụ: `/bookings/123`) |
| `@DeleteMapping("/{id}")` | Xử lý HTTP DELETE |
| `@Valid` | Tự động validate request body theo annotation `@NotNull`, `@NotBlank` |
| `@RequestBody` | Parse JSON body thành Java object |
| `@RequestHeader("X-User-Id")` | Lấy giá trị từ HTTP header `X-User-Id` |
| `@PathVariable` | Lấy giá trị từ URL path (ví dụ: `{id}` = 123) |
| `ResponseEntity` | Cho phép customize HTTP status code (200, 404, 409...) |

---

### 📁 `application/` — Application Layer (Tầng điều phối)

> **Nhiệm vụ:** Điều phối luồng xử lý use case. Gọi domain objects + infrastructure.
> **KHÔNG** chứa business logic (business logic nằm trong domain).

#### `BookingService.java` — Use Case Orchestrator

```java
@Slf4j                      // ← Lombok: tự tạo biến `log` để ghi log
@Service                    // ← Đánh dấu đây là Spring Service bean
public class BookingService {

    // Dependencies được inject qua constructor
    private final BookingRepository bookingRepository;      // ← Domain repository
    private final BookingEventPublisher eventPublisher;     // ← Publish events
    private final RestTemplate restTemplate;                // ← Gọi REST tới service khác
    private final StringRedisTemplate stringRedisTemplate;  // ← Thao tác Redis
    private final Counter heldCounter;                      // ← Metric đếm số booking held

    @Transactional           // ← Bọc method trong 1 DB transaction
    public HoldSeatResponse holdSeat(HoldSeatRequest req, Long userId) {
        // Luồng xử lý use case "Hold Seat":
        //
        // 1. Gọi flight-search-service kiểm tra ghế còn trống
        // 2. Redis SETNX lock ghế (chống 2 người book cùng lúc)
        // 3. Tạo booking với status HELD
        // 4. Lưu vào DB
        // 5. Publish event BookingHeld (qua outbox)
        // 6. Trả kết quả
    }

    @Scheduled(fixedDelay = 60000)   // ← Chạy mỗi 60 giây
    @Transactional
    public void releaseExpiredHolds() {
        // Job tự động: tìm booking HELD quá 10 phút → đổi thành EXPIRED
        // Release Redis lock → ghế trống, người khác book được
    }
}
```

**Các method chính:**

| Method | Use case | Khi nào gọi |
|--------|---------|-------------|
| `holdSeat()` | Giữ ghế 10 phút | User bấm "Chọn ghế" |
| `getBookingById()` | Xem chi tiết booking | User xem đơn đặt vé |
| `getMyBookings()` | Xem danh sách booking của tôi | User vào trang "Đơn của tôi" |
| `cancelBooking()` | Hủy booking | User bấm "Hủy đặt vé" |
| `confirmBooking()` | Xác nhận sau khi thanh toán | **Tự động** — Kafka consume PaymentCompleted |
| `handlePaymentFailed()` | Hủy khi payment fail | **Tự động** — Kafka consume PaymentFailed |
| `releaseExpiredHolds()` | Xóa booking quá hạn | **Tự động** — mỗi 60 giây |

#### `BookingEventPublisher.java` — Publish Domain Events

```java
@Service
public class BookingEventPublisher {
    private final OutboxEventRepository outboxEventRepository;

    public void publishHeld(Booking booking) {
        // KHÔNG gửi trực tiếp lên Kafka!
        // Thay vào đó, INSERT 1 row vào bảng outbox_event (status=PENDING)
        // OutboxRelay sẽ đọc row này và gửi lên Kafka sau
        // → Đây là Outbox Pattern (giải thích bên dưới)
        OutboxEvent event = buildEvent(booking, "BookingHeld");
        outboxEventRepository.save(event);
    }
}
```

#### 📁 `application/dto/` — Data Transfer Objects

**DTO là gì?** Là object dùng để **truyền data** giữa các layer. Khác với domain object (có business logic).

| File | Vai trò | Dùng khi |
|------|---------|---------|
| `HoldSeatRequest.java` | Chứa data từ client gửi lên | Client POST `/hold` gửi JSON `{ flightId, seatNo, passengerName }` |
| `HoldSeatResponse.java` | Chứa data trả về cho client | Server trả `{ bookingId, bookingCode, expiresAt }` |
| `BookingDetailResponse.java` | Chi tiết booking đầy đủ | Client GET `/bookings/123` |

```java
// Record = class immutable, tự tạo constructor + getter + toString
public record HoldSeatRequest(
        @NotNull Long flightId,          // ← Bắt buộc, không được null
        @NotBlank String seatNo,         // ← Bắt buộc, không được rỗng
        @NotBlank String passengerName,
        String passengerPassport         // ← Optional, có thể null
) {}
```

---

### 📁 `domain/` — Domain Layer (Tầng nghiệp vụ — TRÁI TIM)

> **Quy tắc vàng:** Package này KHÔNG ĐƯỢC import Spring, JPA, Redis, Kafka.
> Chỉ chứa Java thuần + business logic.

#### `domain/aggregate/BookingAggregate.java` — Aggregate Root

```java
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class BookingAggregate {
    private Long id;
    private String bookingCode;        // "BK20260630ABC12"
    private Long userId;               // ID user (từ user-service)
    private Long flightId;             // ID chuyến bay (từ flight-search-service)
    private BookingStatus status;      // HELD / CONFIRMED / CANCELLED / EXPIRED
    private BigDecimal totalAmount;    // 1,500,000
    private String currency;           // "VND"
    private LocalDateTime heldAt;      // Thời điểm giữ ghế
    private LocalDateTime expiresAt;   // Hết hạn (heldAt + 10 phút)
    private LocalDateTime confirmedAt; // Thời điểm xác nhận
    private LocalDateTime cancelledAt; // Thời điểm hủy
    private String paymentId;          // ID payment (từ payment-service)
    private Long version;              // Optimistic locking (chống concurrent update)
    private List<BookingItem> items;   // Danh sách items (ghế + hành khách)

    // Business method — PHẢI có validate
    public void confirm(String paymentId) {
        if (this.status != BookingStatus.HELD) {
            throw new InvalidBookingStateException("Chỉ confirm được khi đang HELD");
        }
        this.status = BookingStatus.CONFIRMED;
        this.paymentId = paymentId;
        this.confirmedAt = LocalDateTime.now();
    }
}
```

**Tại sao `userId` và `flightId` chỉ là `Long` (ID) chứ không phải object?**

Vì theo nguyên tắc **Database-per-Service**: booking-service KHÔNG biết chi tiết về User hay Flight. Nó chỉ lưu ID để tham chiếu (gọi là **logical reference**). Khi cần thông tin chi tiết, nó gọi REST API sang service kia.

#### `domain/aggregate/BookingItem.java` — Entity con

```java
public class BookingItem {
    private Long id;
    private String seatNo;              // "12A"
    private String passengerName;       // "Nguyen Van A"
    private String passengerPassport;   // "C12345678"
    private BigDecimal price;           // 1,500,000
}
```

`BookingItem` thuộc về `BookingAggregate`. Không tồn tại độc lập — luôn phải đi kèm 1 booking.

#### `domain/vo/BookingStatus.java` — Value Object

```java
public enum BookingStatus {
    HELD,       // Đang giữ ghế (chờ thanh toán, 10 phút)
    CONFIRMED,  // Đã xác nhận (thanh toán thành công)
    CANCELLED,  // Đã hủy (user hủy hoặc payment fail)
    EXPIRED,    // Hết hạn (quá 10 phút không thanh toán)
    REFUNDED    // Đã hoàn tiền
}
```

**State machine (máy trạng thái):**
```
         ┌──────────── PaymentCompleted ───────────┐
         │                                          ▼
[*] → HELD ──── PaymentFailed ──────────────→ CANCELLED
         │                                          
         ├──── TTL expired (10 min) ──────→ EXPIRED  
         │
         └──── User cancel ───────────────→ CANCELLED

CONFIRMED ──── User request refund ───────→ REFUNDED
```

#### `domain/repository/BookingRepository.java` — Repository Interface (Port)

```java
// Đây CHỈ LÀ INTERFACE — không có implementation ở đây
// Implementation nằm trong infrastructure/adapter/
public interface BookingRepository {
    BookingAggregate save(BookingAggregate aggregate);
    Optional<BookingAggregate> findById(Long id);
    Optional<BookingAggregate> findByBookingCode(String bookingCode);
    Page<BookingAggregate> findByUserId(Long userId, Pageable pageable);
    List<BookingAggregate> findExpiredHolds(BookingStatus status, LocalDateTime now);
}
```

**Tại sao để interface ở domain?** Vì domain layer cần lưu/lấy booking, nhưng KHÔNG CẦN BIẾT dùng PostgreSQL hay MongoDB. Interface này như "hợp đồng" — ai implement cũng được, miễn đúng signature.

---

### 📁 `infrastructure/` — Infrastructure Layer (Tầng kỹ thuật)

> **Nhiệm vụ:** Implement các chi tiết kỹ thuật: database, message broker, external APIs.

#### 📁 `infrastructure/persistence/` — Lưu trữ (Database)

##### `entity/BookingEntity.java` — JPA Entity (mapping bảng DB)

```java
@Entity                              // ← Đánh dấu class này map với 1 bảng trong DB
@Table(name = "booking")             // ← Tên bảng: "booking"
public class BookingEntity {

    @Id                              // ← Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ← Auto-increment
    private Long id;

    @Column(name = "booking_code", nullable = false, unique = true)
    private String bookingCode;

    @Enumerated(EnumType.STRING)     // ← Lưu enum dưới dạng String ("HELD", "CONFIRMED"...)
    private BookingStatus status;

    @Version                         // ← OPTIMISTIC LOCKING (giải thích bên dưới)
    private Long version;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingItemEntity> items;    // ← Quan hệ 1-nhiều với booking_item
}
```

**`@Version` — Optimistic Locking là gì?**

Khi 2 thread cùng đọc booking version=1 và cùng muốn update:
- Thread A: UPDATE booking SET status='CONFIRMED', version=2 WHERE id=1 AND version=1 → ✅ Thành công
- Thread B: UPDATE booking SET status='CANCELLED', version=2 WHERE id=1 AND version=1 → ❌ Fail (version đã là 2)
- Spring throw `OptimisticLockException` → Thread B phải retry

→ **Chống lost update** khi 2 request đồng thời.

**Tại sao có cả `BookingAggregate` và `BookingEntity`?**

| `BookingAggregate` (domain) | `BookingEntity` (infrastructure) |
|-----------------------------|----------------------------------|
| Java thuần, có business logic | JPA annotations (`@Entity`, `@Column`...) |
| Không biết gì về database | Map trực tiếp với bảng `booking` |
| Application Service dùng cái này | JPA Repository dùng cái này |

Cần **mapper** để chuyển đổi qua lại → xem `BookingPersistenceMapper`.

##### `BookingJpaRepository.java` — Spring Data JPA Repository

```java
// Spring Data JPA tự tạo implementation cho các method queries
public interface BookingJpaRepository extends JpaRepository<BookingEntity, Long> {

    // Spring tự sinh SQL: SELECT * FROM booking WHERE booking_code = ?
    Optional<BookingEntity> findByBookingCode(String bookingCode);

    // Spring tự sinh SQL: SELECT * FROM booking WHERE user_id = ? ORDER BY created_at DESC
    Page<BookingEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // Custom JPQL query
    @Query("SELECT b FROM BookingEntity b WHERE b.status = :status AND b.expiresAt < :now")
    List<BookingEntity> findExpiredHolds(@Param("status") BookingStatus status,
                                         @Param("now") LocalDateTime now);
}
```

**Magic của Spring Data JPA:** Chỉ cần khai báo method name theo convention, Spring tự tạo query SQL. Ví dụ:
- `findByBookingCode` → `WHERE booking_code = ?`
- `findByUserIdOrderByCreatedAtDesc` → `WHERE user_id = ? ORDER BY created_at DESC`

##### `mapper/BookingPersistenceMapper.java` — Entity ↔ Domain Mapper

```java
// Chuyển đổi giữa JPA Entity (infrastructure) và Domain Aggregate (domain)
public final class BookingPersistenceMapper {

    // Entity → Aggregate (khi đọc từ DB)
    public static BookingAggregate toAggregate(BookingEntity entity) {
        return BookingAggregate.builder()
                .id(entity.getId())
                .bookingCode(entity.getBookingCode())
                // ... map tất cả fields
                .build();
    }

    // Aggregate → Entity (khi lưu vào DB)
    public static BookingEntity toEntity(BookingAggregate aggregate) {
        // ... ngược lại
    }
}
```

##### `outbox/OutboxEvent.java` — Outbox Event Entity

```java
@Entity
@Table(name = "outbox_event")
public class OutboxEvent {
    private Long id;
    private String aggregateType;     // "Booking"
    private Long aggregateId;         // bookingId
    private String eventType;         // "BookingHeld", "BookingConfirmed"...
    private Map<String, Object> payload;  // JSON data
    private OutboxStatus status;      // PENDING → SENT (hoặc FAILED)
    private Integer retryCount;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}
```

Đây là bảng trung gian cho **Outbox Pattern** (giải thích ở mục 5).

#### 📁 `infrastructure/adapter/` — Repository Adapter

##### `BookingRepositoryAdapter.java` — Implementation của Domain Repository

```java
@Component                // ← Spring bean — được inject vào BookingService
public class BookingRepositoryAdapter implements BookingRepository {
    //                                            ↑ implements domain interface

    private final BookingJpaRepository repository;  // ← Dùng JPA bên trong

    @Override
    public BookingAggregate save(BookingAggregate aggregate) {
        // Domain Aggregate → JPA Entity → save to DB → JPA Entity → Domain Aggregate
        return BookingPersistenceMapper.toAggregate(
                repository.save(BookingPersistenceMapper.toEntity(aggregate)));
    }

    @Override
    public Optional<BookingAggregate> findById(Long id) {
        return repository.findById(id)
                .map(BookingPersistenceMapper::toAggregate);
    }
}
```

**Đây chính là Adapter Pattern**: Domain nói "tôi cần save booking" → Adapter dịch sang "JPA save entity vào PostgreSQL".

#### 📁 `infrastructure/messaging/` — Message Broker (Kafka)

##### `OutboxRelay.java` — Worker gửi events lên Kafka

```java
@Component
public class OutboxRelay {
    private final OutboxEventRepository outboxRepo;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedDelayString = "2000")  // ← Chạy mỗi 2 giây
    @Transactional
    public void relay() {
        // 1. Đọc batch events PENDING từ bảng outbox_event
        // 2. Với mỗi event: xác định Kafka topic (BookingHeld → "booking.held")
        // 3. Gửi lên Kafka
        // 4. Update status → SENT (hoặc FAILED nếu gửi lỗi)
    }
}
```

##### `PaymentEventListener.java` — Lắng nghe events từ Payment Service

```java
@Component
public class PaymentEventListener {

    @KafkaListener(topics = "payment.completed")   // ← Tự động consume từ Kafka topic
    public void onPaymentCompleted(Map<String, Object> payload) {
        // Khi payment-service publish event "payment.completed":
        // → Method này tự động được gọi
        // → Lấy bookingId, paymentCode từ payload
        // → Gọi bookingService.confirmBooking()
        Long bookingId = Long.valueOf(payload.get("bookingId").toString());
        String paymentCode = String.valueOf(payload.get("paymentCode"));
        bookingService.confirmBooking(bookingId, paymentCode);
    }

    @KafkaListener(topics = "payment.failed")
    public void onPaymentFailed(Map<String, Object> payload) {
        // Payment fail → cancel booking (Saga compensation)
        bookingService.handlePaymentFailed(bookingId, reason);
    }
}
```

---

### 📁 `config/` — Cấu hình

#### `HttpRestClient.java`

```java
@Configuration              // ← Đánh dấu class chứa Spring bean definitions
public class HttpRestClient {
    @Bean                   // ← Tạo 1 RestTemplate bean, inject được ở mọi nơi
    public RestTemplate restTemplate() {
        return new RestTemplate();
        // RestTemplate = HTTP client của Spring để gọi REST API
        // Ví dụ: restTemplate.getForObject("http://localhost:8081/api/v1/flights/1", Map.class)
    }
}
```

---

### 📁 `resources/` — Cấu hình ứng dụng

#### `application.yml` — File cấu hình trung tâm

```yaml
server:
  port: 8082                    # ← Booking service chạy trên port 8082

spring:
  application:
    name: booking-service       # ← Tên service (dùng cho Eureka, logging)

  datasource:
    url: jdbc:postgresql://localhost:5432/booking_db    # ← Kết nối PostgreSQL
    username: abs
    password: abs123

  jpa:
    hibernate:
      ddl-auto: validate        # ← Chỉ validate schema, KHÔNG tự tạo bảng
                                #    (Flyway lo việc tạo bảng)
  flyway:
    enabled: true               # ← Dùng Flyway để quản lý DB migration

  data:
    redis:
      host: localhost           # ← Kết nối Redis cho seat-hold lock
      port: 6379

  kafka:
    bootstrap-servers: localhost:9092    # ← Kết nối Kafka
    producer:
      key-serializer: StringSerializer
      value-serializer: JsonSerializer  # ← Gửi JSON lên Kafka
    consumer:
      group-id: booking-service-group   # ← Consumer group (Kafka concept)

booking:
  hold:
    ttl-minutes: 10             # ← Giữ ghế tối đa 10 phút

app:
  kafka:
    topics:                     # ← Tên các Kafka topics
      booking-held: booking.held
      booking-confirmed: booking.confirmed
      payment-completed: payment.completed
      payment-failed: payment.failed
  outbox:
    poll-interval-ms: 2000      # ← OutboxRelay chạy mỗi 2s
    batch-size: 50              # ← Xử lý tối đa 50 events/lần

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka    # ← Đăng ký với Eureka
```

#### `db/migration/V1__init_schema.sql` — Flyway Migration

```sql
-- Flyway đọc file này khi khởi động lần đầu → tạo bảng
-- Tên file PHẢI theo format: V{version}__{description}.sql

CREATE TABLE booking (...);          -- Bảng booking chính
CREATE TABLE booking_item (...);     -- Items (ghế + hành khách)
CREATE TABLE outbox_event (...);     -- Outbox pattern
```

**Flyway là gì?** Tool quản lý version cho database schema. Mỗi file `V1__`, `V2__` là 1 version. Flyway chạy các file chưa chạy theo thứ tự.

---

## 5. Các Design Pattern sử dụng

### 5.1 Database-per-Service Pattern

**Vấn đề:** 5 services dùng chung 1 database → coupling chặt, sửa schema ảnh hưởng tất cả.

**Giải pháp:** Mỗi service có database riêng:
```
flight-search → flight_db
booking       → booking_db
user          → user_db
payment       → payment_db
notification  → notify_db
```

**Hệ quả:** Không có foreign key cross-service. `booking.user_id` chỉ là con số — không có FK constraint tới `users` table (vì nằm ở DB khác).

### 5.2 Repository Pattern

**Vấn đề:** Domain layer không muốn biết dùng PostgreSQL hay MongoDB.

**Giải pháp:**
```
Domain layer:      BookingRepository (interface) ← chỉ biết cái này
                           ↑ implements
Infrastructure:    BookingRepositoryAdapter (dùng JPA + PostgreSQL)
```

### 5.3 Outbox Pattern ⭐

**Vấn đề:** Khi confirm booking, cần đồng thời:
1. UPDATE booking trong DB → status = CONFIRMED
2. Gửi event `BookingConfirmed` lên Kafka

Nếu DB commit thành công nhưng Kafka gửi fail → **data không nhất quán** (booking confirmed nhưng notification không nhận được).

**Giải pháp:** Không gửi trực tiếp lên Kafka. Thay vào đó:

```
Bước 1 (trong 1 DB transaction):
  - UPDATE booking SET status = CONFIRMED
  - INSERT INTO outbox_event (eventType='BookingConfirmed', status='PENDING')
  → 2 thao tác này trong CÙNG 1 transaction → hoặc cả 2 thành công, hoặc cả 2 fail

Bước 2 (OutboxRelay worker, mỗi 2 giây):
  - SELECT * FROM outbox_event WHERE status = 'PENDING'
  - Gửi lên Kafka
  - UPDATE outbox_event SET status = 'SENT'
```

```
┌─────────────────────────────────────────────────────────┐
│ Transaction                                             │
│  UPDATE booking SET status='CONFIRMED'                  │
│  INSERT outbox_event (BookingConfirmed, PENDING)        │
│  → COMMIT cùng lúc, đảm bảo consistency                │
└─────────────────────────────────────────────────────────┘
         │
         ▼  (OutboxRelay worker, mỗi 2s)
┌─────────────────────────────────────────────────────────┐
│ SELECT outbox_event WHERE status='PENDING'              │
│ → kafkaTemplate.send("booking.confirmed", payload)      │
│ → UPDATE outbox_event SET status='SENT'                 │
└─────────────────────────────────────────────────────────┘
```

### 5.4 Choreography Saga Pattern ⭐

**Vấn đề:** Luồng booking → payment → confirm cần nhiều services. Nếu payment fail, phải rollback (release ghế).

**Giải pháp:** Mỗi service **tự phản ứng** theo event nhận được (không có "ông chỉ huy" trung tâm):

```
Happy path:
  booking-service:  CREATE booking (HELD) → publish BookingHeld
  payment-service:  consume BookingHeld → xử lý payment → publish PaymentCompleted
  booking-service:  consume PaymentCompleted → UPDATE booking (CONFIRMED) → publish BookingConfirmed
  notification-svc: consume BookingConfirmed → gửi email

Compensation (payment fail):
  payment-service:  payment fail → publish PaymentFailed
  booking-service:  consume PaymentFailed → UPDATE booking (CANCELLED) + release Redis lock
                    → publish BookingCancelled
  notification-svc: consume BookingCancelled → gửi email "thất bại"
```

**Choreography vs Orchestration:**
| Choreography (dự án này) | Orchestration |
|--------------------------|---------------|
| Mỗi service tự biết phải làm gì khi nhận event | Có 1 "Saga Manager" trung tâm điều phối |
| Đơn giản, ít coupling | Phức tạp hơn, dễ quản lý luồng |
| Phù hợp luồng đơn giản (2-3 bước) | Phù hợp luồng phức tạp (5+ bước) |

### 5.5 Distributed Lock (Redis SETNX) ⭐

**Vấn đề:** 2 user bấm "Giữ ghế 12A" cùng lúc → cả 2 đều thấy ghế trống → double-booking.

**Giải pháp:** Dùng Redis SETNX (Set if Not eXists) — atomic operation:

```
User A:  SETNX seat:VN201:12A = userId_A, TTL=10min  → OK ✅ (key chưa có)
User B:  SETNX seat:VN201:12A = userId_B, TTL=10min  → FAIL ❌ (key đã có)
```

SETNX là **atomic** — dù triệu request đến cùng lúc, Redis đảm bảo chỉ 1 cái thành công.

**2 lớp bảo vệ:**
1. **Redis SETNX** → nhanh, in-memory, chặn ở tầng ứng dụng
2. **DB unique constraint** → backup, chặn ở tầng database (phòng trường hợp Redis die)

### 5.6 Optimistic Locking

**Vấn đề:** 2 thread cùng đọc booking (version=1), cùng update → lost update.

**Giải pháp:** Dùng cột `version`:
```sql
-- Thread A: đọc booking có version=1
-- Thread A: UPDATE booking SET status='CONFIRMED', version=2 WHERE id=1 AND version=1
-- → Thành công (version khớp)

-- Thread B: cũng đọc booking có version=1
-- Thread B: UPDATE booking SET status='CANCELLED', version=2 WHERE id=1 AND version=1
-- → FAIL (version đã là 2, không khớp)
```

Spring JPA tự quản lý bằng `@Version` annotation.

---

## 6. Giao tiếp giữa các services

### 6.1 Đồng bộ (Synchronous) — REST/Feign

**Khi nào dùng:** Cần response ngay lập tức.

```
booking-service ──GET /flights/1/seats/12A──→ flight-search-service
                  ←── { available: true, price: 1500000 } ──
```

**Trong code:**
```java
// Dùng RestTemplate (hiện tại)
Map<String, Object> seatInfo = restTemplate.getForObject(
    "http://localhost:8081/api/v1/flights/" + flightId + "/seats/" + seatNo,
    Map.class);
```

**Nhược điểm:** Nếu flight-search-service chết → booking-service bị block/fail.
**Giải pháp:** Circuit Breaker (Resilience4j) — tự động fail fast khi service kia chết.

### 6.2 Bất đồng bộ (Asynchronous) — Kafka

**Khi nào dùng:** Không cần response ngay. "Fire and forget."

```
booking-service ──publish "booking.confirmed"──→ Kafka ──→ notification-service
                   (không chờ response)              (consume khi nào cũng được)
```

### 6.3 So sánh

| | REST (Đồng bộ) | Kafka (Bất đồng bộ) |
|---|---|---|
| **Tốc độ phản hồi** | Ngay lập tức | Không đảm bảo timing |
| **Coupling** | Chặt (A phải biết B đang sống) | Lỏng (A không biết ai đang lắng nghe) |
| **Reliability** | B chết → A fail | B chết → message chờ trong Kafka, B hồi phục → xử lý tiếp |
| **Dùng khi** | Cần data ngay: check ghế trống, validate user | Thông báo sự kiện: gửi email, update trạng thái |

### 6.4 Kafka Concepts cơ bản

```
Producer (booking-service) → Topic (booking.confirmed) → Consumer (notification-service)
```

| Concept | Giải thích | Ví dụ |
|---------|-----------|-------|
| **Topic** | "Kênh" chứa messages theo chủ đề | `booking.confirmed`, `payment.failed` |
| **Producer** | Service gửi message lên topic | booking-service publish `BookingConfirmed` |
| **Consumer** | Service đọc message từ topic | notification-service consume `BookingConfirmed` |
| **Consumer Group** | Nhóm consumers cùng đọc 1 topic (mỗi message chỉ 1 consumer trong group xử lý) | `booking-service-group` |
| **Partition** | Topic được chia thành N partition để parallel | Partition key = `bookingId` → cùng 1 booking luôn vào cùng partition |

---

## 7. Công nghệ sử dụng — giải thích từng cái

### Framework & Language

| Công nghệ | Là gì | Dùng để làm gì trong dự án |
|-----------|-------|---------------------------|
| **Java 21** | Ngôn ngữ lập trình | Viết toàn bộ backend code |
| **Spring Boot 3.5** | Framework Java | Tự động cấu hình, dependency injection, web server, v.v. |
| **Spring Cloud 2025** | Bộ công cụ cho microservices | Service discovery (Eureka), API Gateway, distributed tracing |
| **Maven** | Build tool | Quản lý dependencies, build project, multi-module |
| **Lombok** | Code generator | Tự tạo getter/setter/constructor/builder → giảm boilerplate |

### Database & Cache

| Công nghệ | Là gì | Dùng để làm gì |
|-----------|-------|----------------|
| **PostgreSQL 16** | Relational database | Lưu booking, booking_item, outbox_event |
| **Redis 7** | In-memory key-value store | Distributed lock cho seat-hold (SETNX + TTL) |
| **Flyway** | DB migration tool | Quản lý version schema (V1__, V2__...) |
| **Spring Data JPA** | ORM framework | Map Java objects ↔ database tables |
| **Hibernate** | JPA implementation | Thực thi JPA spec, generate SQL queries |

### Messaging & Communication

| Công nghệ | Là gì | Dùng để làm gì |
|-----------|-------|----------------|
| **Apache Kafka** | Distributed event streaming | Pub/sub events: `BookingConfirmed`, `PaymentFailed` |
| **Spring Kafka** | Kafka integration cho Spring | `@KafkaListener`, `KafkaTemplate` |
| **RestTemplate** | HTTP client | Gọi REST API tới service khác (flight-search, user) |
| **Eureka** | Service discovery | Services tự đăng ký, Gateway tự tìm services |

### Monitoring & Observability

| Công nghệ | Là gì | Dùng để làm gì |
|-----------|-------|----------------|
| **Micrometer** | Metrics library | Đo lường: bao nhiêu booking/giây, latency... |
| **Prometheus** | Metrics collector | Thu thập metrics từ Micrometer, hiển thị trên Grafana |
| **Zipkin** | Distributed tracing | Theo dõi 1 request đi qua nhiều services |
| **OpenTelemetry** | Observability framework | Chuẩn hóa traces + metrics |
| **Actuator** | Spring Boot monitoring | Expose `/actuator/health`, `/actuator/prometheus` |

### DevOps

| Công nghệ | Là gì | Dùng để làm gì |
|-----------|-------|----------------|
| **Docker** | Container platform | Chạy PostgreSQL, Redis, Kafka trong container |
| **Docker Compose** | Multi-container orchestration | 1 lệnh `docker compose up` → chạy tất cả infrastructure |

---

## 8. Luồng hoạt động end-to-end

### Luồng 1: User đặt vé thành công

```
📱 User                          🔷 Hệ thống
─────                           ────────────
1. Đăng nhập
   POST /auth/login ──────────→ user-service: verify password → trả JWT token
   ←── { accessToken }

2. Tìm chuyến bay
   GET /flights?from=HAN        API Gateway validate JWT
   &to=SGN&date=2026-06-30 ──→ → forward tới flight-search-service
   ←── [VN201, VN203, VJ521]    → query flight_db

3. Chọn ghế & giữ chỗ
   POST /bookings/hold ────────→ booking-service:
   { flightId: 1,                  a. REST call → flight-search: ghế 12A trống? ✅
     seatNo: "12A",                b. Redis SETNX seat:1:12A TTL=10min → ✅ locked
     passengerName: "..." }        c. INSERT booking (status=HELD)
   ←── { bookingId: 1,            d. INSERT outbox_event (BookingHeld, PENDING)
         expiresAt: +10min }       e. OutboxRelay → publish "booking.held" lên Kafka

4. Thanh toán
   POST /payments ─────────────→ payment-service:
   { bookingId: 1,                 a. Mock gateway → SUCCESS
     amount: 1500000 }            b. INSERT payment (status=SUCCESS)
   ←── { paymentId: "PM001" }     c. INSERT outbox_event (PaymentCompleted, PENDING)
                                   d. OutboxRelay → publish "payment.completed" lên Kafka

5. Xác nhận (TỰ ĐỘNG — Saga)
                                 Kafka → booking-service consume "payment.completed":
                                   a. UPDATE booking SET status=CONFIRMED
                                   b. Redis DEL seat:1:12A (release lock)
                                   c. Publish "booking.confirmed" lên Kafka

6. Gửi email (TỰ ĐỘNG)
                                 Kafka → notification-service consume "booking.confirmed":
                                   a. Render email template
   ←── 📧 Email xác nhận           b. Gửi email qua MailHog/SMTP
```

### Luồng 2: Payment fail → Saga compensation

```
📱 User                          🔷 Hệ thống
─────                           ────────────
(Sau bước 3 — đã hold ghế)

4. Thanh toán FAIL
   POST /payments ─────────────→ payment-service:
   { bookingId: 1 }               a. Mock gateway → ❌ DECLINED
                                   b. INSERT payment (status=FAILED)
   ←── 402 Payment Failed          c. Publish "payment.failed" lên Kafka

5. Compensation (TỰ ĐỘNG)
                                 Kafka → booking-service consume "payment.failed":
                                   a. UPDATE booking SET status=CANCELLED ← rollback!
                                   b. Redis DEL seat:1:12A ← giải phóng ghế!
                                   c. Publish "booking.cancelled" lên Kafka

6. Email thất bại (TỰ ĐỘNG)
                                 Kafka → notification-service consume "booking.cancelled":
   ←── 📧 Email "Thanh toán         a. Gửi email thông báo thất bại
        thất bại"
```

### Luồng 3: 2 user tranh ghế (Concurrency)

```
👤 User A                     👤 User B
────────                     ────────
POST /bookings/hold          POST /bookings/hold
(seat 12A)                   (seat 12A)
    │                             │
    ▼                             ▼
booking-service              booking-service
    │                             │
    ▼                             ▼
Redis SETNX seat:1:12A      Redis SETNX seat:1:12A
    │                             │
    ▼                             ▼
   OK ✅ (key chưa có)        FAIL ❌ (key đã có!)
    │                             │
    ▼                             ▼
INSERT booking (HELD)        Return 409 Conflict
Return 200 OK                "Ghế đã được giữ"

→ CHỈ 1 NGƯỜI thành công. User B phải chọn ghế khác.
```

---

## 9. Thuật ngữ thường gặp

| Thuật ngữ | Viết tắt | Nghĩa tiếng Việt | Giải thích |
|-----------|---------|-------------------|-----------|
| **Aggregate Root** | AR | Gốc tổng hợp | Object chính chứa business rules, quản lý nhóm objects liên quan |
| **Bounded Context** | BC | Ngữ cảnh giới hạn | Phạm vi nghiệp vụ mà 1 service chịu trách nhiệm |
| **CRUD** | | Tạo-Đọc-Sửa-Xóa | Create, Read, Update, Delete — 4 thao tác cơ bản |
| **DTO** | | Đối tượng truyền dữ liệu | Data Transfer Object — chứa data gửi/nhận qua API |
| **Entity** | | Thực thể | Object có ID riêng (identity) |
| **Value Object** | VO | Đối tượng giá trị | Object không có ID, so sánh bằng giá trị |
| **Repository** | Repo | Kho lưu trữ | Interface để lưu/lấy domain objects |
| **Saga** | | | Pattern quản lý transaction phân tán |
| **Outbox** | | Hộp thư đi | Pattern đảm bảo event publish nhất quán với DB |
| **Idempotent** | | Bất biến | Gọi N lần cho kết quả giống 1 lần (chống double-submit) |
| **TTL** | | Thời gian sống | Time-To-Live — thời gian key tồn tại trong Redis |
| **Optimistic Lock** | OL | Khóa lạc quan | Dùng version number để detect concurrent update |
| **Circuit Breaker** | CB | Cầu dao | Tự động ngắt call khi service kia chết, fail fast |
| **Consumer Group** | CG | Nhóm consumer | Kafka: nhiều consumers chia nhau xử lý messages |
| **JPA** | | | Java Persistence API — chuẩn ORM của Java |
| **ORM** | | | Object-Relational Mapping — map Java ↔ DB |
| **Bean** | | | Object do Spring quản lý lifecycle |
| **Dependency Injection** | DI | Tiêm phụ thuộc | Spring tự tạo và truyền dependencies vào class |
| **@Transactional** | | | Bọc method trong DB transaction (commit/rollback tự động) |
| **Migration** | | Di chuyển schema | Script thay đổi cấu trúc DB (Flyway V1__, V2__...) |

### Annotation cheat sheet

| Annotation | Ở đâu | Làm gì |
|-----------|-------|--------|
| `@SpringBootApplication` | Main class | Đánh dấu entry point, tự scan components |
| `@RestController` | Controller | Xử lý REST API, auto JSON serialize |
| `@Service` | Service class | Đánh dấu Spring service bean |
| `@Component` | Any class | Đánh dấu Spring-managed bean |
| `@Configuration` | Config class | Chứa `@Bean` definitions |
| `@Bean` | Method | Tạo 1 Spring bean |
| `@RequiredArgsConstructor` | Class (Lombok) | Tự tạo constructor cho `final` fields |
| `@Getter/@Setter` | Class (Lombok) | Tự tạo getter/setter |
| `@Builder` | Class (Lombok) | Tạo builder pattern |
| `@Slf4j` | Class (Lombok) | Tạo biến `log` cho logging |
| `@Entity` | JPA Entity | Map class với bảng DB |
| `@Table(name="...")` | JPA Entity | Chỉ định tên bảng |
| `@Id` | Field | Primary key |
| `@GeneratedValue` | Field | Auto-increment |
| `@Column` | Field | Map với cột DB |
| `@Version` | Field | Optimistic locking |
| `@OneToMany` | Field | Quan hệ 1-nhiều |
| `@Transactional` | Method/Class | Bọc trong DB transaction |
| `@Scheduled` | Method | Chạy định kỳ (cron/fixed delay) |
| `@KafkaListener` | Method | Tự động consume Kafka messages |
| `@Valid` | Parameter | Kích hoạt bean validation |
| `@NotNull` | Field (DTO) | Validation: không được null |
| `@NotBlank` | Field (DTO) | Validation: không được rỗng |
| `@Value("${...}")` | Field | Inject giá trị từ application.yml |

---

## Tóm tắt

> **Microservices** = Chia hệ thống thành nhiều service nhỏ, độc lập, giao tiếp qua API/events.
>
> **DDD** = Thiết kế code theo nghiệp vụ thực tế. Business logic nằm trong Domain layer (aggregate), không rải rác.
>
> **Booking-service** là Core Domain — phức tạp nhất vì:
> 1. **State machine** (HELD → CONFIRMED → CANCELLED)
> 2. **Distributed lock** (Redis SETNX chống double-booking)
> 3. **Saga** (choreography với payment-service)
> 4. **Outbox pattern** (đảm bảo event consistency)
> 5. **Optimistic locking** (chống concurrent update)

Khi code, luôn tự hỏi:
- **Logic này thuộc domain hay infrastructure?** → Domain = business rules, Infrastructure = database/Redis/Kafka
- **Method này nên ở aggregate hay service?** → Validate state transition → aggregate. Điều phối nhiều thứ → service
- **Giao tiếp sync hay async?** → Cần response ngay → REST. Thông báo sự kiện → Kafka
