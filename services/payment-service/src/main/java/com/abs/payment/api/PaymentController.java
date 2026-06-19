package com.abs.payment.api;

import com.abs.payment.application.port.in.CreateSePayPaymentUseCase;
import com.abs.payment.application.dto.CreatePaymentRequest;
import com.abs.payment.application.dto.PaymentResponse;
import com.abs.payment.domain.aggregate.PaymentAggregate;
import com.abs.payment.domain.repository.PaymentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final CreateSePayPaymentUseCase service;
    private final PaymentRepository repo;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody CreatePaymentRequest req) {
        PaymentAggregate p = service.createSePayPayment(req);
        return ResponseEntity.ok(PaymentResponse.of(p, service.buildQrUrl(p)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getOne(@PathVariable Long id) {
        return repo.findById(id)
                .map(p -> ResponseEntity.ok(PaymentResponse.of(p, service.buildQrUrl(p))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-code/{code}")
    public ResponseEntity<PaymentResponse> getByCode(@PathVariable String code) {
        return repo.findByPaymentCode(code)
                .map(p -> ResponseEntity.ok(PaymentResponse.of(p, service.buildQrUrl(p))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-booking/{bookingId}")
    public List<PaymentResponse> byBooking(@PathVariable Long bookingId) {
        return repo.findByBookingId(bookingId).stream()
                .map(p -> PaymentResponse.of(p, service.buildQrUrl(p)))
                .toList();
    }
}
