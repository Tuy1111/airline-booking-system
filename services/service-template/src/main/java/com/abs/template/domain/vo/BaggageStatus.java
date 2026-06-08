package com.abs.template.domain.vo;

import com.abs.template.domain.aggregate.BaggageAggregate;

/**
 * Vòng đời của một kiện hành lý. Việc chuyển trạng thái được kiểm soát bởi
 * aggregate {@link BaggageAggregate} — không ai được set trạng thái tuỳ tiện từ bên ngoài.
 */
public enum BaggageStatus {
    /** Đã ký gửi hợp lệ, trong hạn mức. */
    CHECKED_IN,
    /** Vượt hạn mức — giữ lại chờ xử lý (thu phí / loại bớt). */
    OVERWEIGHT_HELD,
    /** Đã được xếp lên tàu bay. */
    LOADED,
    /** Đã gỡ khỏi chuyến (huỷ ký gửi / chuyển chuyến). */
    REMOVED
}
