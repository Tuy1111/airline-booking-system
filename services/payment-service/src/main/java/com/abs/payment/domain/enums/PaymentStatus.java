package com.abs.payment.domain.enums;

public enum PaymentStatus {
    PENDING, SUCCESS, FAILED;

    /** Trạng thái cuối — không được chuyển tiếp (dùng cho idempotency của webhook). */
    public boolean isTerminal() {
        return this == SUCCESS || this == FAILED;
    }
}
