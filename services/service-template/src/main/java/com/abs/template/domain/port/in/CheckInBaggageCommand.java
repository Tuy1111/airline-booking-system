package com.abs.template.domain.port.in;

import java.math.BigDecimal;

/**
 * Command — dữ liệu đầu vào của {@link CheckInBaggageUseCase}, đã được làm sạch.
 *
 * <p>Self-validating ngay tại constructor: lõi không bao giờ nhận command "rác".
 * Đây là DTO của tầng port, độc lập với JSON request của web (adapter map qua).
 */
public record CheckInBaggageCommand(
        String bookingRef,
        BigDecimal weightKg,
        BigDecimal allowanceKg
) {
    public CheckInBaggageCommand {
        if (bookingRef == null || bookingRef.isBlank()) {
            throw new IllegalArgumentException("bookingRef must not be blank");
        }
        if (weightKg == null || weightKg.signum() < 0) {
            throw new IllegalArgumentException("weightKg must be >= 0");
        }
        if (allowanceKg == null || allowanceKg.signum() <= 0) {
            throw new IllegalArgumentException("allowanceKg must be > 0");
        }
    }
}
