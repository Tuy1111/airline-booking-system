package com.abs.template.domain.vo;

import com.abs.template.domain.aggregate.BaggageAggregate;
import java.util.Objects;
import java.util.UUID;

/**
 * Value Object — định danh của aggregate {@link BaggageAggregate}.
 *
 * <p>Dùng kiểu riêng thay cho {@code UUID}/{@code Long} trần để tránh "primitive
 * obsession": compiler chặn việc truyền nhầm một id khác vào đúng chỗ này.
 * Immutable & so sánh theo giá trị (đặc trưng của Value Object).
 */
public record BaggageId(UUID value) {

    public BaggageId {
        Objects.requireNonNull(value, "BaggageId.value must not be null");
    }

    /** Sinh id mới cho aggregate vừa tạo. */
    public static BaggageId newId() {
        return new BaggageId(UUID.randomUUID());
    }

    /** Khôi phục id từ chuỗi (vd: khi đọc từ DB hoặc path variable). */
    public static BaggageId of(String raw) {
        return new BaggageId(UUID.fromString(raw));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
