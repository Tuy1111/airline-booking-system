-- payment-service · initial schema
-- Tham chiếu docs/design.md §II.5

CREATE TABLE payment (
    id              BIGSERIAL     PRIMARY KEY,
    payment_code    VARCHAR(30)   NOT NULL UNIQUE,
    booking_id      BIGINT        NOT NULL,                -- logical ref → booking-service
    user_id         BIGINT        NOT NULL,                -- logical ref → user-service
    amount          DECIMAL(12,2) NOT NULL,
    currency        VARCHAR(3)    NOT NULL DEFAULT 'VND',
    method          VARCHAR(20)   NOT NULL,
    status          VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    idempotency_key VARCHAR(100)  NOT NULL UNIQUE,
    created_at      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    completed_at    TIMESTAMP,
    CONSTRAINT ck_payment_status CHECK (status IN ('PENDING','SUCCESS','FAILED')),
    CONSTRAINT ck_payment_method CHECK (method IN ('CARD','WALLET','BANK_TRANSFER'))
);

CREATE INDEX idx_payment_booking ON payment(booking_id);
CREATE INDEX idx_payment_user    ON payment(user_id, created_at DESC);

CREATE TABLE transaction (
    id               BIGSERIAL    PRIMARY KEY,
    payment_id       BIGINT       NOT NULL REFERENCES payment(id) ON DELETE CASCADE,
    gateway_txn_id   VARCHAR(100),
    gateway_response TEXT,
    status           VARCHAR(20)  NOT NULL,
    created_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE refund (
    id              BIGSERIAL     PRIMARY KEY,
    payment_id      BIGINT        NOT NULL REFERENCES payment(id),
    amount          DECIMAL(12,2) NOT NULL,
    reason          VARCHAR(255),
    status          VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    created_at      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_refund_status CHECK (status IN ('PENDING','SUCCESS','FAILED'))
);

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
