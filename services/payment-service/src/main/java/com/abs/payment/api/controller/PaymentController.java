package com.abs.payment.api.controller;

import com.abs.payment.api.dto.CreatePaymentRequest;
import com.abs.payment.api.dto.PaymentResponse;
import com.abs.payment.application.port.in.CreateSePayPaymentUseCase;
import com.abs.payment.domain.aggregate.Payment;
import com.abs.payment.domain.repository.PaymentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPath.PAYMENTS)
@RequiredArgsConstructor
public class PaymentController {

    private final CreateSePayPaymentUseCase service;
    private final PaymentRepository repo;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody CreatePaymentRequest req) {
        Payment p = service.createSePayPayment(req);
        return ResponseEntity.ok(PaymentResponse.of(p, service.buildQrUrl(p)));
    }

    @GetMapping(ApiPath.BY_ID)
    public ResponseEntity<PaymentResponse> getOne(@PathVariable Long id) {
        return repo.findById(id)
                .map(p -> ResponseEntity.ok(PaymentResponse.of(p, service.buildQrUrl(p))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(ApiPath.BY_CODE)
    public ResponseEntity<PaymentResponse> getByCode(@PathVariable String code) {
        return repo.findByPaymentCode(code)
                .map(p -> ResponseEntity.ok(PaymentResponse.of(p, service.buildQrUrl(p))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(ApiPath.BY_BOOKING)
    public List<PaymentResponse> byBooking(@PathVariable Long bookingId) {
        return repo.findByBookingId(bookingId).stream()
                .map(p -> PaymentResponse.of(p, service.buildQrUrl(p)))
                .toList();
    }
}
