-- user-service · V2 — loyalty, KYC, and account-security fields
-- Extends the V1 schema to back the rich User aggregate: frequent-flyer loyalty (status tier +
-- spendable/lifetime miles), passport KYC verification, and failed-login / GDPR-erasure tracking.

-- ----- users: account security + GDPR + loyalty -----
ALTER TABLE users
    ADD COLUMN failed_login_attempts  INT         NOT NULL DEFAULT 0,
    ADD COLUMN deleted_at             TIMESTAMP,
    ADD COLUMN loyalty_tier           VARCHAR(20) NOT NULL DEFAULT 'BLUE',
    ADD COLUMN loyalty_miles          BIGINT      NOT NULL DEFAULT 0,
    ADD COLUMN loyalty_lifetime_miles BIGINT      NOT NULL DEFAULT 0,
    ADD COLUMN enrolled_at            TIMESTAMP;

-- Backfill enrolment timestamp for any pre-existing accounts.
UPDATE users SET enrolled_at = COALESCE(created_at, CURRENT_TIMESTAMP) WHERE enrolled_at IS NULL;

ALTER TABLE users
    ADD CONSTRAINT ck_users_tier  CHECK (loyalty_tier IN ('BLUE','SILVER','GOLD','PLATINUM')),
    ADD CONSTRAINT ck_users_miles CHECK (loyalty_miles >= 0 AND loyalty_lifetime_miles >= 0);

CREATE INDEX idx_users_loyalty_tier ON users(loyalty_tier);

-- ----- passenger: passport details + KYC status -----
ALTER TABLE passenger
    ADD COLUMN passport_country VARCHAR(3),
    ADD COLUMN passport_expiry  DATE,
    ADD COLUMN kyc_status       VARCHAR(20) NOT NULL DEFAULT 'UNVERIFIED';

ALTER TABLE passenger
    ADD CONSTRAINT ck_passenger_kyc CHECK (kyc_status IN ('UNVERIFIED','PENDING','VERIFIED','REJECTED'));
