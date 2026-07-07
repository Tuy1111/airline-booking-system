package com.abs.booking.api;

import com.abs.booking.application.BookingService;
import com.abs.booking.application.dto.BookingDetailResponse;
import com.abs.booking.application.dto.HoldSeatRequest;
import com.abs.booking.application.dto.HoldSeatResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/hold")
    public ResponseEntity<HoldSeatResponse> holdSeat(
            @Valid @RequestBody HoldSeatRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        HoldSeatResponse response = bookingService.holdSeat(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDetailResponse> getBooking(@PathVariable("id") Long id) {
        BookingDetailResponse response = bookingService.getBookingById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Page<BookingDetailResponse>> getMyBookings(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        Page<BookingDetailResponse> response = bookingService.getMyBookings(userId, status, page, size);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookingDetailResponse> cancelBooking(
            @PathVariable("id") Long id,
            @RequestHeader("X-User-Id") Long userId) {
        BookingDetailResponse response = bookingService.cancelBooking(id, userId);
        return ResponseEntity.ok(response);
    }
}
