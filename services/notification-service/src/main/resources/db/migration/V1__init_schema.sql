-- notification-service · initial schema
-- Tham chiếu docs/design.md §II.6

CREATE TABLE notification_template (
    code        VARCHAR(50)  NOT NULL,
    locale      VARCHAR(5)   NOT NULL DEFAULT 'vi',
    channel     VARCHAR(10)  NOT NULL,
    subject     VARCHAR(255),
    body        TEXT         NOT NULL,
    PRIMARY KEY (code, locale, channel),
    CONSTRAINT ck_template_channel CHECK (channel IN ('EMAIL','SMS','PUSH'))
);

CREATE TABLE notification (
    id              BIGSERIAL    PRIMARY KEY,
    template_code   VARCHAR(50)  NOT NULL,
    user_id         BIGINT,                     -- logical ref → user-service
    channel         VARCHAR(10)  NOT NULL,
    recipient       VARCHAR(100) NOT NULL,
    variables       JSONB,
    status          VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    retry_count     INT          DEFAULT 0,
    error_message   TEXT,
    created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    sent_at         TIMESTAMP,
    CONSTRAINT ck_notification_status  CHECK (status IN ('PENDING','SENT','FAILED')),
    CONSTRAINT ck_notification_channel CHECK (channel IN ('EMAIL','SMS','PUSH'))
);

CREATE INDEX idx_notif_user    ON notification(user_id, created_at DESC);
CREATE INDEX idx_notif_pending ON notification(status) WHERE status = 'PENDING';

CREATE TABLE flight_reminder_log (
    booking_id  BIGINT     PRIMARY KEY,
    flight_id   BIGINT     NOT NULL,
    sent_at     TIMESTAMP  NOT NULL
);
