-- booking-service · initial schema
-- Tham chiếu docs/design.md §II.4

CREATE TABLE booking (
    id              BIGSERIAL     PRIMARY KEY,
    booking_code    VARCHAR(20)   NOT NULL UNIQUE,
    user_id         BIGINT        NOT NULL,    -- logical ref → user-service
    flight_id       BIGINT        NOT NULL,    -- logical ref → flight-search-service
    status          VARCHAR(20)   NOT NULL,
    total_amount    DECIMAL(12,2) NOT NULL,
    currency        VARCHAR(3)    NOT NULL DEFAULT 'VND',
    held_at         TIMESTAMP,
    expires_at      TIMESTAMP,
    confirmed_at    TIMESTAMP,
    cancelled_at    TIMESTAMP,
    payment_id      VARCHAR(50),                -- logical ref → payment-service
    version         BIGINT        NOT NULL DEFAULT 0,
    created_at      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_booking_status CHECK (status IN ('HELD','CONFIRMED','CANCELLED','EXPIRED','REFUNDED'))
);

CREATE INDEX idx_booking_user    ON booking(user_id, created_at DESC);
CREATE INDEX idx_booking_status  ON booking(status);
CREATE INDEX idx_booking_expires ON booking(expires_at) WHERE status = 'HELD';

CREATE TABLE booking_item (
    id                 BIGSERIAL    PRIMARY KEY,
    booking_id         BIGINT       NOT NULL REFERENCES booking(id) ON DELETE CASCADE,
    seat_no            VARCHAR(5)   NOT NULL,
    passenger_name     VARCHAR(100) NOT NULL,
    passenger_passport VARCHAR(20),
    price              DECIMAL(12,2) NOT NULL
);

CREATE UNIQUE INDEX uk_booking_item_seat ON booking_item(booking_id, seat_no);

-- Outbox pattern — đảm bảo at-least-once delivery cho Kafka events
CREATE TABLE outbox_event (
    id             BIGSERIAL    PRIMARY KEY,
    aggregate_type VARCHAR(50)  NOT NULL,
    aggregate_id   BIGINT       NOT NULL,
    event_type     VARCHAR(50)  NOT NULL,
    payload        JSONB        NOT NULL,
    status         VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    retry_count    INT          DEFAULT 0,
    created_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    sent_at        TIMESTAMP,
    CONSTRAINT ck_outbox_status CHECK (status IN ('PENDING','SENT','FAILED'))
);

CREATE INDEX idx_outbox_pending ON outbox_event(status, created_at) WHERE status = 'PENDING';
