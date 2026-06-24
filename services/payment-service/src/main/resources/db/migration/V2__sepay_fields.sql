-- payment-service · V2 · SePay (bank transfer) integration

ALTER TABLE payment
    ADD COLUMN gateway        VARCHAR(20),
    ADD COLUMN transfer_code  VARCHAR(30),
    ADD COLUMN reference_code VARCHAR(100),
    ADD COLUMN expires_at     TIMESTAMP,
    ADD COLUMN failure_reason VARCHAR(500);

-- gateway = SEPAY | MOCK | ...
CREATE UNIQUE INDEX ux_payment_transfer_code   ON payment(transfer_code)  WHERE transfer_code  IS NOT NULL;
CREATE UNIQUE INDEX ux_payment_reference_code  ON payment(reference_code) WHERE reference_code IS NOT NULL;
