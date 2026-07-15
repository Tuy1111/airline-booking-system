-- notification-service · V3
-- (1) Idempotency cho consumer: khoá trùng theo dedup_key (eventType:bookingCode)
--     -> Kafka redelivery / rebalance không gửi email trùng.
-- (2) Bổ sung template SMS còn thiếu (V2 chỉ seed EMAIL) -> sendSms không còn
--     luôn ném "SMS template not found".

ALTER TABLE notification ADD COLUMN dedup_key VARCHAR(150);

CREATE UNIQUE INDEX ux_notification_dedup
    ON notification(dedup_key) WHERE dedup_key IS NOT NULL;

INSERT INTO notification_template (code, locale, channel, subject, body) VALUES
('BOOKING_CONFIRMED', 'vi', 'SMS', NULL,
 'ABS: Ve {{bookingCode}} da xac nhan. Chuyen {{flightNo}} {{from}}-{{to}} luc {{departureTime}}, ghe {{seatNo}}.');
