# service-template — Hexagonal + DDD skeleton

Khuôn mẫu để tạo **service mới** theo kiến trúc **Hexagonal (Ports & Adapters)** kết hợp
**DDD chiến thuật**. Chạy được ngay với H2 in-memory, không cần Docker/Postgres.

```bash
cd services/service-template
./mvnw spring-boot:run
# POST http://localhost:8090/api/baggage   {"bookingRef":"BK-1","weightKg":20,"allowanceKg":23}
# GET  http://localhost:8090/api/baggage/{id}
# H2:  http://localhost:8090/h2-console
```

## Quy tắc phụ thuộc (Dependency Rule)

Mọi dependency trỏ **vào trong**. Domain không biết gì ngoài chính nó.

```
        adapter.in.web ─┐                       ┌─ adapter.out.persistence
                        ├──> application ──> domain <──┤
   (REST, driving)     ─┘     (use case)    (lõi)      └─ adapter.out.messaging
                                                          (JPA / Kafka, driven)
```

- **domain** — Java thuần, KHÔNG Spring/JPA. Aggregate chứa hành vi + invariant; định nghĩa port.
- **application** — hiện thực inbound port, điều phối 1 use case, quản lý transaction. Mỏng.
- **adapter** — công nghệ cụ thể, phụ thuộc domain qua interface (port).

## Bản đồ thư mục

```
domain/
  aggregate/  BaggageAggregate (Aggregate Root)
  vo/         Weight + BaggageId (Value Object), BaggageStatus
  event/      DomainEvent, BaggageCheckedIn, BaggageOverweightFlagged
  port/in/    CheckInBaggageUseCase, GetBaggageUseCase, CheckInBaggageCommand   ← driving
  repository/ BaggageRepository                                                  ← driven
  port/out/   DomainEventPublisher                                                ← driven
  exception/  BaggageNotFoundException, BaggageStateException
application/
  BaggageApplicationService          (implements 2 inbound port)
adapter/
  in/web/     BaggageController, GlobalExceptionHandler, dto/{Request,Response}
  out/persistence/  BaggageJpaEntity, SpringDataBaggageRepository,
                    BaggagePersistenceAdapter (implements port), BaggageMapper
  out/messaging/    LoggingDomainEventPublisher (implements port)
config/       wiring tường minh khi cần
```

## Vì sao tách JPA entity khỏi aggregate?

`BaggageAggregate` (domain) và `BaggageJpaEntity` (persistence) là **hai class khác nhau**,
nối bằng `BaggageMapper`. Nhờ vậy annotation JPA không rò vào lõi, schema DB tiến hoá độc
lập với mô hình nghiệp vụ, và domain unit-test được không cần context
(xem `BaggageAggregateTest`).

## Tạo service mới từ template

1. Copy thư mục `services/service-template` → `services/<tên>-service`.
2. Đổi `com.abs.template` → `com.abs.<tên>`, đổi `artifactId`/`name` trong `pom.xml`,
   đổi tên class `ServiceTemplateApplication`.
3. Thay bounded context mẫu (`Baggage`) bằng aggregate thật của bạn.
4. Thêm `<module>services/<tên>-service</module>` vào `pom.xml` gốc.
5. **Lên production**: đổi H2 → Postgres + Flyway và `LoggingDomainEventPublisher` →
   adapter Kafka/Outbox (tham chiếu `payment-service` / `notification-service`); chỉ sửa
   `pom.xml`, `application.yml` và lớp adapter — **domain & application không đổi**.
```
