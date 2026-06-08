package com.abs.template.domain.port.in;

import com.abs.template.domain.aggregate.BaggageAggregate;
import com.abs.template.domain.vo.BaggageId;

/** Inbound Port (driving) — truy vấn một kiện hành lý theo id. */
public interface GetBaggageUseCase {
    BaggageAggregate getById(BaggageId id);
}
