package com.abs.template.domain.exception;

import com.abs.template.domain.vo.BaggageId;

/** Không tìm thấy aggregate theo id. */
public class BaggageNotFoundException extends RuntimeException {
    public BaggageNotFoundException(BaggageId id) {
        super("Baggage not found: " + id);
    }
}
