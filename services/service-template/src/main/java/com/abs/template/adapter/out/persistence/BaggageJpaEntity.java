package com.abs.template.adapter.out.persistence;

import com.abs.template.domain.vo.BaggageStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * JPA Entity — mô hình <b>lưu trữ</b>, KHÁC với aggregate domain {@code Baggage}.
 *
 * <p>Tách đôi như vậy để annotation/ràng buộc của JPA không rò vào lõi domain, và để
 * cấu trúc bảng được tự do tiến hoá độc lập với mô hình nghiệp vụ. {@link BaggageMapper}
 * lo việc ánh xạ hai chiều.
 */
@Entity
@Table(name = "baggage")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BaggageJpaEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "booking_ref", nullable = false, length = 30)
    private String bookingRef;

    @Column(name = "weight_kg", nullable = false, precision = 6, scale = 2)
    private BigDecimal weightKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BaggageStatus status;
}
