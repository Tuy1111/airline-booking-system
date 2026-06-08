package com.abs.template.adapter.in.web;

import com.abs.template.domain.exception.BaggageNotFoundException;
import com.abs.template.domain.exception.BaggageStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Dịch exception của domain thành mã HTTP — giữ cho controller sạch và mapping nhất quán.
 * Dùng {@link ProblemDetail} (RFC 7807).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaggageNotFoundException.class)
    public ProblemDetail handleNotFound(BaggageNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({BaggageStateException.class, IllegalArgumentException.class})
    public ProblemDetail handleBadRequest(RuntimeException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
