package com.abs.template.domain.exception;

/** Vi phạm quy tắc chuyển trạng thái của aggregate (vd: load hành lý đã REMOVED). */
public class BaggageStateException extends RuntimeException {
    public BaggageStateException(String message) {
        super(message);
    }
}
