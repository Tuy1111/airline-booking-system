-- Seed các template thông báo mặc định (VN, EMAIL)

INSERT INTO notification_template (code, locale, channel, subject, body) VALUES
('BOOKING_CONFIRMED', 'vi', 'EMAIL',
 'Xác nhận đặt vé {{bookingCode}}',
 'Xin chào {{passengerName}},\n\nVé của bạn đã được xác nhận.\n\nMã đặt chỗ: {{bookingCode}}\nChuyến bay: {{flightNo}} ({{from}} → {{to}})\nKhởi hành: {{departureTime}}\nGhế: {{seatNo}}\nTổng tiền: {{amount}} {{currency}}\n\nChúc bạn có chuyến bay an toàn!'
),
('BOOKING_CANCELLED', 'vi', 'EMAIL',
 'Vé {{bookingCode}} đã bị hủy',
 'Xin chào {{passengerName}},\n\nVé {{bookingCode}} của bạn đã được hủy.\nLý do: {{reason}}\n\nNếu có thanh toán, hệ thống sẽ hoàn tiền trong 3-5 ngày làm việc.'
),
('FLIGHT_REMINDER', 'vi', 'EMAIL',
 'Nhắc nhở: Chuyến bay {{flightNo}} ngày mai',
 'Xin chào {{passengerName}},\n\nChuyến bay {{flightNo}} từ {{from}} đến {{to}} sẽ khởi hành lúc {{departureTime}}.\n\nVui lòng có mặt tại sân bay trước giờ bay 2 tiếng.\nGhế: {{seatNo}}\n\nChúc bạn có chuyến đi tốt lành!'
),
('PAYMENT_FAILED', 'vi', 'EMAIL',
 'Thanh toán cho {{bookingCode}} thất bại',
 'Xin chào {{passengerName}},\n\nThanh toán cho đặt chỗ {{bookingCode}} đã thất bại.\nLý do: {{reason}}\n\nGhế của bạn đã được giải phóng. Vui lòng thử lại với phương thức thanh toán khác.'
);
