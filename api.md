# Airline Booking System API

<!-- Tài liệu được đối chiếu với controller, DTO và Gateway security trong source code. -->

Cập nhật: 2026-07-19

## 1. Tổng quan

Mọi API nghiệp vụ từ trình duyệt phải đi qua API Gateway. Keycloak chạy riêng trên cổng `8180`.

| Thành phần | Local URL | Mục đích |
|---|---|---|
| API Gateway | `http://localhost:8080` | Gọi các API `/api/v1/**` |
| Keycloak | `http://localhost:8180` | Đăng nhập, đăng ký, refresh token và đăng xuất |
| Keycloak realm | `http://localhost:8180/realms/airline-booking` | Issuer dùng để xác thực JWT |

Ở production, API Gateway thường dùng cùng origin với frontend, ví dụ `https://bimat.click`; URL Keycloak lấy từ biến môi trường `KEYCLOAK_URL`.

Prefix chung của REST API là `/api/v1`.

Các cổng `8081`–`8085` của service chỉ được expose trong mạng Docker ở production. Client không gọi trực tiếp các service và không tự gửi các header `X-User-Id`, `X-User-Email`, `X-User-Roles`; Gateway sẽ xóa các giá trị do client cung cấp rồi tạo lại từ access token Keycloak.

## 2. Xác thực và phân quyền

Frontend đăng nhập, đăng ký, refresh token và đăng xuất trực tiếp qua Keycloak bằng Authorization Code Flow + PKCE. User-service không còn endpoint `/login`, `/register` hoặc API phát hành JWT.

Cấu hình frontend local:

```env
VITE_KEYCLOAK_URL=http://localhost:8180
VITE_KEYCLOAK_REALM=airline-booking
VITE_KEYCLOAK_CLIENT_ID=airline-frontend
```

Với endpoint được bảo vệ, gửi access token:

```http
Authorization: Bearer <keycloak-access-token>
```

Quy ước quyền:

| Ký hiệu | Yêu cầu |
|---|---|
| Public | Không cần token |
| User | Token Keycloak hợp lệ |
| Admin | Token Keycloak có realm role `ADMIN` |
| SePay | Header `Authorization: Apikey <SEPAY_API_KEY>` |

Nếu token thiếu hoặc không hợp lệ, Gateway trả `401`. Nếu token hợp lệ nhưng thiếu role cần thiết, Gateway trả `403`.

## 3. Danh sách endpoint

### 3.1 Flight Search Service

#### Tra cứu chuyến bay

| Method | Path | Quyền | Mô tả |
|---|---|---|---|
| GET | `/api/v1/flights` | Public | Tìm chuyến bay |
| GET | `/api/v1/flights/upcoming` | Public | Chuyến bay sắp khởi hành |
| GET | `/api/v1/flights/{id}` | Public | Chi tiết chuyến bay |
| GET | `/api/v1/flights/{flightId}/seats` | Public | Sơ đồ ghế |
| GET | `/api/v1/flights/{flightId}/seats/{seatNo}` | Public | Kiểm tra một ghế |
| GET | `/api/v1/flights/airports` | Public | Danh sách sân bay, alias cũ |
| GET | `/api/v1/flights/airlines` | Public | Danh sách hãng bay, alias cũ |

Query của `GET /api/v1/flights`:

| Tham số | Bắt buộc | Mặc định | Ghi chú |
|---|---:|---|---|
| `from` | Có | — | Mã IATA sân bay đi |
| `to` | Có | — | Mã IATA sân bay đến |
| `date` | Có | — | Ngày `YYYY-MM-DD` |
| `passengers` | Không | `1` | Số hành khách, tối thiểu 1 |
| `status` | Không | — | `SCHEDULED`, `DELAYED`, `CANCELLED`, `DEPARTED` |
| `airline` | Không | — | Mã hãng bay |
| `minPrice` | Không | — | Giá tối thiểu |
| `maxPrice` | Không | — | Giá tối đa |
| `dateTo` | Không | — | Ngày kết thúc `YYYY-MM-DD` |
| `sort` | Không | `departureTime` | Trường sắp xếp |
| `order` | Không | `asc` | `asc` hoặc `desc` |

Ví dụ:

```bash
curl "http://localhost:8080/api/v1/flights?from=HAN&to=SGN&date=2026-07-20&passengers=1"
```

`GET /api/v1/flights/upcoming` nhận query `hours`, mặc định `24`.

Flight search response:

```json
{
  "id": 10,
  "flightNo": "VN123",
  "airlineCode": "VN",
  "airlineName": "Vietnam Airlines",
  "fromAirport": "HAN",
  "fromCity": "Ha Noi",
  "toAirport": "SGN",
  "toCity": "Ho Chi Minh City",
  "departureTime": "2026-07-20T09:00:00",
  "arrivalTime": "2026-07-20T11:10:00",
  "basePrice": 1400000,
  "currentPrice": 1540000,
  "aircraftType": "Airbus A321",
  "status": "SCHEDULED",
  "availableSeats": 42
}
```

Seat response:

```json
{
  "flightId": 10,
  "seatNo": "12A",
  "seatClass": "ECONOMY",
  "status": "AVAILABLE",
  "price": 1540000
}
```

Giá trị ghế: class `ECONOMY`, `BUSINESS`, `FIRST`; status `AVAILABLE`, `HELD`, `BOOKED`.

#### Quản trị chuyến bay

| Method | Path | Quyền | Body/Kết quả |
|---|---|---|---|
| POST | `/api/v1/flights` | Admin | Tạo chuyến bay, trả `201` |
| PUT | `/api/v1/flights/{id}` | Admin | Cập nhật chuyến bay |
| DELETE | `/api/v1/flights/{id}` | Admin | Xóa chuyến bay, trả `204` |
| POST | `/api/v1/flights/import` | Admin | Import danh sách chuyến bay |
| PUT | `/api/v1/flights/{id}/status` | Admin | Cập nhật trạng thái/lịch bay |
| PUT | `/api/v1/flights/{flightId}/seats/{seatNo}/hold` | Admin | Giữ ghế nội bộ |
| PUT | `/api/v1/flights/{flightId}/seats/{seatNo}/book` | Admin | Đánh dấu ghế đã đặt |
| PUT | `/api/v1/flights/{flightId}/seats/{seatNo}/release` | Admin | Trả ghế về trạng thái trống |

Body tạo chuyến bay:

```json
{
  "flightNo": "VN123",
  "fromAirportCode": "HAN",
  "toAirportCode": "SGN",
  "airlineCode": "VN",
  "departureTime": "2026-07-20T09:00:00",
  "arrivalTime": "2026-07-20T11:10:00",
  "totalSeats": 180,
  "basePrice": 1400000,
  "aircraftType": "Airbus A321"
}
```

Body cập nhật chuyến bay; tất cả field đều có thể bỏ qua:

```json
{
  "departureTime": "2026-07-20T09:30:00",
  "arrivalTime": "2026-07-20T11:40:00",
  "basePrice": 1500000,
  "aircraftType": "Airbus A321",
  "totalSeats": 180
}
```

Body cập nhật trạng thái:

```json
{
  "status": "DELAYED",
  "newDepartureTime": "2026-07-20T10:00:00",
  "newArrivalTime": "2026-07-20T12:10:00"
}
```

#### Airport, Airline và Route

| Method | Path | Quyền | Mô tả |
|---|---|---|---|
| GET | `/api/v1/airports` | Public | Danh sách sân bay |
| POST | `/api/v1/airports` | Admin | Tạo sân bay |
| PUT | `/api/v1/airports/{code}` | Admin | Cập nhật sân bay |
| GET | `/api/v1/airlines` | Public | Danh sách hãng bay |
| POST | `/api/v1/airlines` | Admin | Tạo hãng bay |
| GET | `/api/v1/routes` | Public | Danh sách đường bay |
| POST | `/api/v1/routes` | Admin | Tạo đường bay |

Request mẫu:

`AirportRequest`:

```json
{
  "iataCode": "HAN",
  "name": "Noi Bai International Airport",
  "city": "Ha Noi",
  "country": "Vietnam"
}
```

`AirlineRequest`:

```json
{
  "code": "VN",
  "name": "Vietnam Airlines"
}
```

`RouteRequest`:

```json
{
  "fromAirport": "HAN",
  "toAirport": "SGN",
  "distanceKm": 1160
}
```

### 3.2 Booking Service

Tất cả endpoint booking yêu cầu token Keycloak. `X-User-Id` được Gateway tạo từ danh tính Keycloak.

| Method | Path | Quyền | Mô tả |
|---|---|---|---|
| POST | `/api/v1/bookings/hold` | User | Giữ ghế và tạo booking |
| GET | `/api/v1/bookings/me` | User | Booking của người đang đăng nhập |
| GET | `/api/v1/bookings/{id}` | User | Chi tiết booking |
| DELETE | `/api/v1/bookings/{id}` | User | Hủy booking của chính user |
| GET | `/api/v1/bookings/ping` | User | Kiểm tra booking-service |
| GET | `/api/v1/bookings/ping-flight` | User | Kiểm tra kết nối tới flight service |

Body giữ ghế:

```json
{
  "flightId": 10,
  "seatNo": "12A",
  "passengerName": "Nguyen Van A",
  "passengerPassport": "P1234567"
}
```

Response giữ ghế:

```json
{
  "bookingId": 25,
  "bookingCode": "BK20260720ABCDE",
  "flightId": 10,
  "seatNo": "12A",
  "price": 1540000,
  "currency": "VND",
  "holdExpiresAt": "2026-07-20T08:10:00",
  "message": "Seat held successfully. Please complete payment before expiry."
}
```

Query của `/bookings/me`: `status` không bắt buộc; `page=0`; `size=20`. Booking status gồm `HELD`, `CONFIRMED`, `CANCELLED`, `EXPIRED`, `REFUNDED`.

Booking detail gồm: `id`, `bookingCode`, `userId`, `flightId`, `status`, `totalAmount`, `currency`, các mốc thời gian, `paymentId`, và danh sách `items` với `seatNo`, `passengerName`, `passengerPassport`, `price`.

### 3.3 Payment Service

| Method | Path | Quyền | Mô tả |
|---|---|---|---|
| POST | `/api/v1/payments` | User | Tạo payment SePay |
| GET | `/api/v1/payments/{id}` | User | Lấy payment theo ID |
| GET | `/api/v1/payments/by-code/{code}` | User | Lấy payment theo payment code |
| GET | `/api/v1/payments/by-booking/{bookingId}` | User | Danh sách payment của booking |
| POST | `/api/v1/payments/webhooks/sepay` | SePay | Nhận webhook giao dịch ngân hàng |

Body tạo payment không nhận `userId`; service lấy user hiện tại từ Gateway:

```json
{
  "bookingId": 25,
  "amount": 1540000,
  "idempotencyKey": "web-25-1720000000000",
  "method": "BANK_TRANSFER"
}
```

`amount` tối thiểu `1000`. `method` là `CARD`, `WALLET` hoặc `BANK_TRANSFER`; nếu bỏ qua thì mặc định `BANK_TRANSFER`.

Payment response:

```json
{
  "id": 40,
  "paymentCode": "PAYABCDEFGHIJ",
  "bookingId": 25,
  "amount": 1540000,
  "currency": "VND",
  "status": "PENDING",
  "gateway": "SEPAY",
  "transferCode": "ABS25XYZ",
  "qrUrl": "https://...",
  "expiresAt": "2026-07-20T08:20:00"
}
```

Payment status: `PENDING`, `SUCCESS`, `FAILED`.

Webhook SePay:

```bash
curl -X POST "http://localhost:8080/api/v1/payments/webhooks/sepay" \
  -H "Authorization: Apikey <SEPAY_API_KEY>" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 123456,
    "gateway": "MBBank",
    "transactionDate": "2026-07-20 08:00:00",
    "accountNumber": "0123456789",
    "code": "ABS25XYZ",
    "content": "Thanh toan ABS25XYZ",
    "transferType": "in",
    "transferAmount": 1540000,
    "accumulated": 5000000,
    "subAccount": null,
    "referenceCode": "FT123456",
    "description": "Bank transfer"
  }'
```

Webhook thành công trả:

```json
{ "success": true, "applied": true }
```

### 3.4 Notification Service

Notification chỉ cung cấp API tra cứu; gửi email/SMS được kích hoạt qua Kafka/RabbitMQ.

| Method | Path | Quyền | Mô tả |
|---|---|---|---|
| GET | `/api/v1/notifications/me` | User | Thông báo của user hiện tại |
| GET | `/api/v1/notifications` | Admin | Tất cả thông báo |
| GET | `/api/v1/notifications/{id}` | Admin | Chi tiết thông báo |
| GET | `/api/v1/notifications/by-user/{userId}` | Admin | Thông báo theo user ID |

Các endpoint danh sách nhận `page=0`, `size=20`. Response sử dụng Spring `Page`.

Notification item:

```json
{
  "id": 80,
  "templateCode": "BOOKING_CONFIRMED",
  "userId": 1,
  "channel": "EMAIL",
  "recipient": "user@example.com",
  "status": "SENT",
  "retryCount": 0,
  "errorMessage": null,
  "createdAt": "2026-07-20T08:00:00",
  "sentAt": "2026-07-20T08:00:02"
}
```

Channel: `EMAIL`, `SMS`, `PUSH`. Status: `PENDING`, `SENT`, `FAILED`.

### 3.5 User Service

User-service chỉ quản lý hồ sơ, loyalty và KYC. Đăng nhập, đăng ký và đăng xuất thuộc Keycloak.

| Method | Path | Quyền | Mô tả |
|---|---|---|---|
| GET | `/api/v1/users/{id}` | User | Thông tin phẳng cho tích hợp nội bộ |
| GET | `/api/v1/users/{id}/profile` | User | Hồ sơ đầy đủ |
| PUT | `/api/v1/users/{id}/profile` | User | Cập nhật hồ sơ |
| POST | `/api/v1/users/{id}/miles/earn` | User | Cộng dặm |
| POST | `/api/v1/users/{id}/miles/redeem` | User | Đổi dặm |
| POST | `/api/v1/users/{id}/passport` | User | Gửi hộ chiếu để KYC |
| POST | `/api/v1/users/{id}/passport/verify` | User | Duyệt hộ chiếu |
| POST | `/api/v1/users/{id}/passport/reject` | User | Từ chối hộ chiếu |
| DELETE | `/api/v1/users/{id}?reason=...` | User | Xóa mềm/ẩn danh hóa tài khoản, trả `204` |

Body cập nhật profile:

```json
{
  "fullName": "Nguyen Van A",
  "phone": "0900000000",
  "dateOfBirth": "1995-05-20",
  "gender": "MALE",
  "nationality": "VNM"
}
```

Body cộng/đổi dặm:

```json
{
  "miles": 1000,
  "reason": "Completed flight VN123"
}
```

Body gửi hộ chiếu:

```json
{
  "passportNumber": "P1234567",
  "issuingCountry": "VNM",
  "expiryDate": "2030-12-31"
}
```

User profile response gồm:

```text
id, email, status, roles, fullName, phone, dateOfBirth, gender, nationality,
passportNumber, passportCountry, passportExpiry, kycStatus, loyaltyTier,
milesBalance, lifetimeMiles, createdAt, lastLoginAt
```

Lưu ý: `GET /api/v1/users/{id}` hiện trả dữ liệu stub nếu không tìm thấy user để giữ tương thích với booking-service.

## 4. Pagination

Các API dùng Spring `Page` trả cấu trúc tương tự:

```json
{
  "content": [],
  "pageable": {},
  "totalElements": 0,
  "totalPages": 0,
  "size": 20,
  "number": 0,
  "first": true,
  "last": true,
  "numberOfElements": 0,
  "empty": true
}
```

## 5. Error response

Security error từ Gateway thường dùng HTTP status `401` hoặc `403`.

Booking-service trả lỗi dạng:

```json
{
  "code": "SEAT_ALREADY_HELD",
  "message": "Seat is already held",
  "timestamp": "2026-07-20T08:00:00"
}
```

Các code booking chính:

| HTTP | Code |
|---:|---|
| 400 | `SEAT_NOT_AVAILABLE`, `INVALID_BOOKING_STATE`, `DOMAIN_ERROR` |
| 403 | `UNAUTHORIZED_ACCESS` |
| 404 | `BOOKING_NOT_FOUND` |
| 409 | `SEAT_ALREADY_HELD` |
| 500 | `INTERNAL_SERVER_ERROR` |

User-service trả lỗi dạng:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "User not found"
}
```

## 6. Ví dụ gọi API có xác thực

```bash
export ACCESS_TOKEN="<keycloak-access-token>"

curl "http://localhost:8080/api/v1/bookings/me?page=0&size=20" \
  -H "Authorization: Bearer $ACCESS_TOKEN"
```

Không đưa access token thật, client secret, API key hoặc mật khẩu vào source code hay tài liệu được commit.
