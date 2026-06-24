package com.abs.template.domain.repository;

import com.abs.template.domain.aggregate.BaggageAggregate;
import com.abs.template.domain.vo.BaggageId;

import java.util.Optional;

/**
 * Outbound Port (driven) — kho lưu trữ aggregate {@link BaggageAggregate}.
 *
 * <p><b>Đặt trong domain là điểm mấu chốt của Hexagonal:</b> domain định nghĩa
 * <i>cái nó cần</i> (lưu / tìm aggregate), còn adapter persistence ở ngoài mới
 * hiện thực <i>bằng cách nào</i> (JPA/Postgres…). Dependency vì thế trỏ vào trong
 * (Dependency Inversion) — domain không hề biết tới JPA.
 */
public interface BaggageRepository {

    BaggageAggregate save(BaggageAggregate baggage);

    Optional<BaggageAggregate> findById(BaggageId id);
}
