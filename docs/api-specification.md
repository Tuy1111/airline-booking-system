# Flight Search Service — API Specification

*Cập nhật gần nhất: 05/07/2026*

Tài liệu này đặc tả chi tiết **21 API endpoints** hiện có của `flight-search-service` phục vụ cho hệ thống đặt vé máy bay (bao gồm các API gốc và 16 API mới bổ sung ở các mức ưu tiên P0, P1, P2).

Tất cả các API được ánh xạ thông qua WebConfig với tiền tố chung (context path prefix) là `/api/v1`.

---

## 🔴 Nhóm API P0 — Nghiệp Vụ Ghế & Đặt Chỗ (Seat Actions)

### 1. Giữ Chỗ (Hold Seat)
Thực hiện giữ ghế cho hành khách trong thời gian chờ thanh toán (mặc định 10 phút).
- **Endpoint:** `PUT /api/v1/flights/{flightId}/seats/{seatNo}/hold`
- **Method:** `PUT`
- **Path Variables:**
  - `flightId` (Long): ID chuyến bay.
  - `seatNo` (String): Số ghế (ví dụ: `1A`, `4C`).
- **Response (200 OK):**
  ```json
  {
    "flightId": 7,
    "seatNo": "1A",
    "seatClass": "BUSINESS",
    "status": "HELD",
    "price": 1800000
  }
  ```
- **Error Response (409 Conflict):** Trả về khi ghế đã bị giữ hoặc đã đặt bởi người khác.
  ```json
  {
    "type": "about:blank",
    "title": "Conflict",
    "status": 409,
    "detail": "Ghế không ở trạng thái khả dụng để giữ chỗ",
    "instance": "/api/v1/flights/7/seats/1A/hold"
  }
  ```

### 2. Xác Nhận Đặt Ghế (Book Seat)
Xác nhận đặt ghế chính thức sau khi thanh toán thành công.
- **Endpoint:** `PUT /api/v1/flights/{flightId}/seats/{seatNo}/book`
- **Method:** `PUT`
- **Response (200 OK):** Ghế chuyển sang trạng thái `BOOKED` và áp dụng giá vé tăng động (Dynamic Pricing) theo thuật toán chiếm chỗ.
  ```json
  {
    "flightId": 7,
    "seatNo": "1A",
    "seatClass": "BUSINESS",
    "status": "BOOKED",
    "price": 1803001
  }
  ```

### 3. Giải Phóng Ghế (Release Seat)
Hủy giữ chỗ hoặc hoàn vé, đưa trạng thái ghế về lại `AVAILABLE`.
- **Endpoint:** `PUT /api/v1/flights/{flightId}/seats/{seatNo}/release`
- **Method:** `PUT`
- **Response (200 OK):**
  ```json
  {
    "flightId": 7,
    "seatNo": "1A",
    "seatClass": "BUSINESS",
    "status": "AVAILABLE",
    "price": 1800000
  }
  ```

---

## 🟡 Nhóm API P1 — Trạng Thái & Chuyến Bay Sắp Khởi Hành

### 4. Cập Nhật Trạng Thái Chuyến Bay (Update Flight Status)
Cập nhật trạng thái (DELAYED, CANCELLED, DEPARTED, SCHEDULED). Cần cung cấp giờ mới khi delay.
- **Endpoint:** `PUT /api/v1/flights/{id}/status`
- **Method:** `PUT`
- **Body (`FlightStatusUpdateRequest`):**
  ```json
  {
    "status": "DELAYED",
    "newDepartureTime": "2026-07-06T11:00:00",
    "newArrivalTime": "2026-07-06T13:00:00"
  }
  ```
- **Response (200 OK):**
  ```json
  {
    "id": 7,
    "flightNo": "VN123",
    "airlineCode": "VN",
    "airlineName": "Vietnam Airlines",
    "fromAirport": "HAN",
    "toAirport": "SGN",
    "departureTime": "2026-07-06T11:00:00",
    "arrivalTime": "2026-07-06T13:00:00",
    "status": "DELAYED",
    "availableSeats": 60,
    "totalSeats": 60
  }
  ```

### 5. Truy Vấn Chuyến Bay Sắp Khởi Hành (Upcoming Flights)
Lấy danh sách các chuyến bay sắp cất cánh (SCHEDULED / DELAYED) trong vòng N giờ tới.
- **Endpoint:** `GET /api/v1/flights/upcoming`
- **Method:** `GET`
- **Params:**
  - `hours` (int, default = `24`): Số giờ tới cần quét.
- **Response (200 OK):**
  ```json
  [
    {
      "id": 7,
      "flightNo": "VN123",
      "airlineCode": "VN",
      "airlineName": "Vietnam Airlines",
      "fromAirport": "HAN",
      "toAirport": "SGN",
      "departureTime": "2026-07-06T11:00:00",
      "arrivalTime": "2026-07-06T13:00:00",
      "status": "DELAYED",
      "availableSeats": 60
    }
  ]
  ```

### 6. Tìm Kiếm Chuyến Bay Nâng Cao (Search Flights)
Hỗ trợ tìm kiếm cơ bản và lọc nâng cao theo khoảng giá, hãng bay, khoảng ngày và sắp xếp.
- **Endpoint:** `GET /api/v1/flights`
- **Method:** `GET`
- **Params:**
  - `from` (String, required): Mã IATA sân bay đi.
  - `to` (String, required): Mã IATA sân bay đến.
  - `date` (LocalDate, required): Ngày bay dạng `YYYY-MM-DD`.
  - `passengers` (int, default = 1): Số lượng hành khách.
  - `status` (FlightStatus, optional): Trạng thái chuyến bay.
  - `airline` (String, optional): Mã hãng bay.
  - `minPrice` / `maxPrice` (BigDecimal, optional): Khoảng giá.
  - `dateTo` (LocalDate, optional): Ngày kết thúc nếu muốn quét khoảng ngày.
  - `sort` (String, default = `departureTime`): Sắp xếp theo `price`, `departureTime`, `arrivalTime`.
  - `order` (String, default = `asc`): Thứ tự `asc` hoặc `desc`.
- **Response (200 OK):** Danh sách các chuyến bay thỏa mãn bộ lọc.

---

## 🟢 Nhóm API P2 — Quản Trị Hệ Thống (Admin CRUD)

### 7. Tạo Chuyến Bay (Create Flight)
Khởi tạo chuyến bay mới và tự động sinh layout ghế lưới (A-F), hàng 1-3 là Business, hàng 4 trở đi là Economy.
- **Endpoint:** `POST /api/v1/flights`
- **Method:** `POST`
- **Body (`FlightCreateRequest`):**
  ```json
  {
    "flightNo": "VN123",
    "fromAirportCode": "HAN",
    "toAirportCode": "SGN",
    "airlineCode": "VN",
    "departureTime": "2026-07-06T10:00:00",
    "arrivalTime": "2026-07-06T12:00:00",
    "totalSeats": 60,
    "basePrice": 1200000,
    "aircraftType": "Airbus A321"
  }
  ```
- **Response (201 Created):** Chi tiết chuyến bay vừa tạo.

### 8. Cập Nhật Chuyến Bay (Update Flight)
Cập nhật thông tin chi tiết của chuyến bay và tổng số ghế.
- **Endpoint:** `PUT /api/v1/flights/{id}`
- **Method:** `PUT`
- **Body (`FlightUpdateRequest`):**
  ```json
  {
    "departureTime": "2026-07-06T11:00:00",
    "arrivalTime": "2026-07-06T13:00:00",
    "basePrice": 1500000,
    "aircraftType": "Airbus A321",
    "totalSeats": 72
  }
  ```
- **Response (200 OK):** Thông tin chuyến bay sau khi cập nhật.

### 9. Xóa Chuyến Bay (Delete Flight)
Xóa chuyến bay khỏi hệ thống.
- **Endpoint:** `DELETE /api/v1/flights/{id}`
- **Method:** `DELETE`
- **Response (204 No Content):** Không có nội dung trả về.

### 10. Quản Lý Sân Bay (Airports)
- **Lấy danh sách sân bay:** `GET /api/v1/airports`
- **Tạo sân bay:** `POST /api/v1/airports`
  - Body: `{"iataCode": "HPH", "name": "Cat Bi", "city": "Hai Phong", "country": "VN"}`
- **Cập nhật sân bay:** `PUT /api/v1/airports/{code}`

### 11. Quản Lý Hãng Bay (Airlines)
- **Lấy danh sách hãng bay:** `GET /api/v1/airlines`
- **Tạo hãng bay:** `POST /api/v1/airlines`
  - Body: `{"code": "VU", "name": "Vietravel Airlines"}`

### 12. Quản Lý Tuyến Bay (Routes)
- **Lấy danh sách tuyến bay:** `GET /api/v1/routes`
- **Tạo tuyến bay:** `POST /api/v1/routes`
  - Body: `{"fromAirport": "HAN", "toAirport": "DAD", "distanceKm": 605}`

### 13. Nhập Lô Chuyến Bay (Import Flights)
- **Endpoint:** `POST /api/v1/flights/import`
- **Method:** `POST`
- **Body:** Mảng JSON các `FlightImportRequest` chứa thông tin sân bay, hãng bay và chi tiết chuyến bay để import hàng loạt.
- **Response (200 OK):** Trả về rỗng.

---

## 🔍 Nhóm API Truy Vấn Sẵn Có (Core Queries)
- **Lấy chi tiết chuyến bay:** `GET /api/v1/flights/{id}`
- **Lấy bản đồ ghế của chuyến bay:** `GET /api/v1/flights/{flightId}/seats`
- **Xem trạng thái chi tiết của 1 ghế cụ thể:** `GET /api/v1/flights/{flightId}/seats/{seatNo}`
