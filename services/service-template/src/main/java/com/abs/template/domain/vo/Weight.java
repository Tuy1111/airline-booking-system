package com.abs.template.domain.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object — khối lượng hành lý (kilogram).
 *
 * <p>Bao trọn một invariant nhỏ ("không âm") và một quy tắc nghiệp vụ
 * ({@link #exceeds}) ngay tại nơi dữ liệu sống, thay vì rải rác trong service.
 * Đây là điểm khác cốt lõi so với "anemic model".
 */
public record Weight(BigDecimal kilograms) {

    public Weight {
        Objects.requireNonNull(kilograms, "kilograms must not be null");
        if (kilograms.signum() < 0) {
            throw new IllegalArgumentException("Weight must not be negative: " + kilograms);
        }
        kilograms = kilograms.setScale(2, RoundingMode.HALF_UP);
    }

    public static Weight ofKg(double kg) {
        return new Weight(BigDecimal.valueOf(kg));
    }

    public static Weight ofKg(BigDecimal kg) {
        return new Weight(kg);
    }

    /** True nếu cân nặng này vượt quá hạn mức cho phép. */
    public boolean exceeds(Weight allowance) {
        return this.kilograms.compareTo(allowance.kilograms) > 0;
    }
}
