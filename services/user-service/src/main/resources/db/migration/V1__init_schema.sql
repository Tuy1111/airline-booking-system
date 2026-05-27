-- user-service · initial schema
-- Tham chiếu docs/design.md §II.3

CREATE TABLE users (
    id              BIGSERIAL    PRIMARY KEY,
    email           VARCHAR(100) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    last_login_at   TIMESTAMP,
    CONSTRAINT ck_users_status CHECK (status IN ('ACTIVE','LOCKED','DELETED'))
);

CREATE INDEX idx_users_email ON users(email);

CREATE TABLE passenger (
    user_id         BIGINT       PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    full_name       VARCHAR(100) NOT NULL,
    phone           VARCHAR(20),
    date_of_birth   DATE,
    gender          VARCHAR(10),
    passport_no     VARCHAR(20),
    nationality     VARCHAR(3),
    CONSTRAINT ck_passenger_gender CHECK (gender IS NULL OR gender IN ('MALE','FEMALE','OTHER'))
);

CREATE TABLE user_role (
    user_id     BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role        VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id, role),
    CONSTRAINT ck_user_role CHECK (role IN ('USER','ADMIN'))
);
