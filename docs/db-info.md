# Giải Thích Chi Tiết Mục Đích Các Bảng Trong Hệ Thống Airline Booking

Tài liệu này phân tích chi tiết mục đích sinh ra của từng bảng trong kiến trúc **Database-per-Service** của hệ thống Đặt vé Máy bay, giúp làm rõ vai trò của từng thành phần trong vận hành hệ thống.

---

## 1. flight_db (flight-search-service)
*Dịch vụ này chịu trách nhiệm quản lý thông tin tuyến bay, lịch trình, trạng thái chuyến bay và số lượng ghế trống thực tế.*

* **`airport` (Sân bay):** Lưu danh mục các sân bay trên thế giới (Mã IATA, tên, thành phố, quốc gia). Được sinh ra để kiểm tra tính hợp lệ của điểm đi/điểm đến và hiển thị thông tin trực quan lên giao diện tìm kiếm chuyến bay cho người dùng.
* **`airline` (Hãng hàng không):** Lưu danh mục các hãng vận chuyển (Ví dụ: VN - Vietnam Airlines, VJ - VietJet Air). Giúp phân loại và hiển thị chuyến bay thuộc hãng nào trên giao diện.
* **`route` (Tuyến bay):** Định nghĩa một chặng bay cụ thể kết nối giữa 2 sân bay (điểm đi, điểm đến và khoảng cách địa lý). Sinh ra nhằm mục đích tối ưu hóa việc tìm kiếm và lọc các chuyến bay có chung lộ trình hành trình.
* **`flight` (Chuyến bay):** Bảng cốt lõi lưu lịch trình chi tiết của một chuyến bay cụ thể (mã hiệu, giờ cất/hạ cánh, giá vé cơ bản, loại máy bay, trạng thái). Trường `version` áp dụng kỹ thuật **Optimistic Locking (Khóa lạc quan)** để ngăn chặn việc 2 tiến trình cập nhật trạng thái chuyến bay cùng một lúc gây đè dữ liệu.
* **`seat_inventory` (Kho ghế tổng hợp):** Lưu tổng số ghế, số ghế còn trống, số ghế đang bị giữ (held), và số ghế đã bán của một chuyến bay. Việc tách riêng dữ liệu này khỏi bảng `flight` giúp tối ưu hóa hiệu năng ghi (write path) khi hệ thống phải xử lý hàng trăm request cập nhật số lượng ghế đồng thời.
* **`flight_seat` (Sơ đồ ghế chi tiết):** Quản lý trạng thái của từng vị trí ghế ngồi cụ thể (ví dụ: 12A, 12B) kèm theo hạng vé (Economy, Business) và hệ số nhân giá. Sinh ra để phục vụ tính năng cho phép hành khách tự chọn chỗ ngồi chính xác trên máy bay.

---

## 2. user_db (user-service)
*Dịch vụ này chịu trách nhiệm quản lý tài khoản người dùng, phân quyền và lưu trữ thông tin cá nhân/hộ chiếu.*

* **`users` (Tài khoản):** Lưu thông tin đăng nhập tối thiểu (email, hash mật khẩu bằng BCrypt, trạng thái tài khoản). Đảm bảo tính an toàn bảo mật và xử lý các logic Đăng ký/Đăng nhập (Authentication).
* **`passenger` (Hồ sơ hành khách):** Lưu thông tin cá nhân chi tiết (họ tên, số điện thoại, ngày sinh, số hộ chiếu, quốc tịch). Bảng này được tách rời khỏi bảng `users` để cho phép một tài khoản người dùng có thể quản lý hoặc đặt vé hộ cho nhiều hành khách khác nhau trong tương lai.
* **`user_role` (Quyền hạn):** Phân quyền cho tài khoản (Ví dụ: `USER` hoặc `ADMIN`). Sinh ra để làm màng lọc kiểm soát truy cập (Authorization), chặn các truy cập trái phép vào các API quản trị hệ thống.

---

## 3. booking_db (booking-service) ⭐️ Core Schema
*Trung tâm của toàn bộ hệ thống, quản lý vòng đời của một đơn đặt vé và áp dụng mô hình Transactional Outbox để giao tiếp bất đồng bộ.*

* **`booking` (Đơn đặt vé):** Lưu thông tin tổng quan của một giao dịch đặt vé (mã đặt chỗ public, trạng thái đơn hàng như HELD/CONFIRMED/CANCELLED, tổng tiền, thời gian hết hạn giữ chỗ).
* **`booking_item` (Chi tiết vé đặt):** Lưu danh sách hành khách và số ghế cụ thể đi kèm với mã `booking`. Đặc biệt, một `UNIQUE INDEX` được thiết lập trên hai trường `(booking_id, seat_no)` đóng vai trò là "chốt chặn cuối cùng" ở tầng DB, **phòng chống tuyệt đối lỗi Overbooking** (hai người đặt trùng một ghế trên một chuyến bay).
* **`outbox_event` (Hộp thư sự kiện đi):** Hiện thực hóa **Transactional Outbox Pattern**. Thay vì vừa cập nhật DB vừa gọi Kafka trực tiếp (dễ gây bất đồng bộ dữ liệu nếu Kafka gặp sự cố), hệ thống sẽ ghi Event vào bảng này trong cùng một DB Transaction. Một Worker nội bộ sẽ quét bảng này để đẩy lên Kafka sau, bảo đảm sự kiện luôn được gửi đi ít nhất một lần (At-least-once delivery).

---

## 4. payment_db (payment-service)
*Quản lý toàn bộ dòng tiền, trạng thái giao dịch với các cổng thanh toán bên ngoài và lịch sử hoàn tiền.*

* **`payment` (Hóa đơn thanh toán):** Lưu thông tin yêu cầu thanh toán cho một `booking_id`. Trường `idempotency_key` (khóa ID duy nhất do Client sinh) sinh ra nhằm mục đích **chống thanh toán trùng lặp** nếu người dùng bấm nút thanh toán nhiều lần liên tiếp do mạng lag.
* **`transaction` (Lịch sử giao dịch):** Ghi lại chi tiết từng phản hồi (Response log) từ cổng thanh toán bên ngoài (Payment Gateway). Sinh ra làm bằng chứng dữ liệu để phục vụ việc đối soát tài chính khi có tranh chấp hoặc lỗi hệ thống.
* **`refund` (Yêu cầu hoàn tiền):** Lưu thông tin khi khách hàng hủy vé hợp lệ và hệ thống tiến hành trả lại tiền (số tiền hoàn, lý do hoàn, trạng thái xử lý).
* **`outbox_event` (Hộp thư sự kiện thanh toán):** Tương tự như ở dịch vụ booking, bảng này lưu các sự kiện (ví dụ: `PaymentCompletedEvent`) để thông báo an toàn sang cho `booking-service` xử lý tiếp luồng nghiệp vụ.

---

## 5. notify_db (notification-service)
*Quản lý việc truyền thông, gửi tin nhắn/email tới khách hàng và đảm bảo tính chính xác của tần suất gửi tin.*

* **`notification_template` (Mẫu thông báo):** Lưu các tiêu đề và nội dung mẫu (sử dụng ngôn ngữ định dạng như Mustache/Thymeleaf) theo từng ngôn ngữ (vi/en) và kênh gửi (Email/SMS). Sinh ra để đội ngũ vận hành dễ dàng thay đổi nội dung tin nhắn mà không cần phải can thiệp vào mã nguồn hệ thống.
* **`notification` (Nhật ký gửi tin):** Lưu lịch sử tất cả các thông báo đã hoặc đang chờ được gửi cho khách hàng, bao gồm trạng thái (Thành công/Thất bại), số lần thử lại (retry_count) và log lỗi chi tiết phục vụ việc giám sát (monitoring).
* **`flight_reminder_log` (Nhật ký nhắc nhở chuyến bay):** Lưu vết thông tin xem chuyến bay nào đã được gửi tin nhắn nhắc nhở trước 24 giờ chưa. Bảng này sinh ra như một chốt chặn nghiệp vụ, **ngăn việc gửi trùng tin nhắn nhắc nhở** cho hành khách khi tiến trình Cron Job (Scheduler) quét dữ liệu định kỳ mỗi giờ.

---

## 💡 Tổng Kết Về Mối Liên Kết Giữa Các Cơ Sở Dữ Liệu
Vì hệ thống tuân thủ nghiêm ngặt nguyên tắc **Database-per-Service**, giữa các DB hoàn toàn không có Foreign Key vật lý. Các trường ID xuất hiện chéo như `user_id` (ở booking, payment, notification), `flight_id` (ở booking) hay `booking_id` (ở payment, notification) chỉ đóng vai trò là **mối liên kết logic** (Loosely coupled). Hệ thống sẽ sử dụng các mã ID này để truy vấn thông tin chi tiết thông qua các API REST (đồng bộ) hoặc xử lý luồng dữ liệu thông qua Kafka Message (bất đồng bộ).