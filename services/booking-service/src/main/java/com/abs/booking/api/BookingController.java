package com.abs.booking.api;

import com.abs.booking.application.dto.BookingDetailResponse;
import com.abs.booking.application.dto.HoldSeatRequest;
import com.abs.booking.application.dto.HoldSeatResponse;
import com.abs.booking.application.usecase.*;
import com.abs.booking.domain.vo.BookingStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final HoldSeatUseCase holdSeatUseCase;
    private final GetBookingByIdUseCase getBookingByIdUseCase;
    private final GetMyBookingsUseCase getMyBookingsUseCase;
    private final GetAllBookingsUseCase getAllBookingsUseCase;
    private final CancelBookingUseCase cancelBookingUseCase;

    @PostMapping("/hold")
    public ResponseEntity<HoldSeatResponse> holdSeat(
            @Valid @RequestBody HoldSeatRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        HoldSeatResponse response = holdSeatUseCase.execute(request, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDetailResponse> getBooking(@PathVariable Long id) {
        BookingDetailResponse response = getBookingByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Page<BookingDetailResponse>> getMyBookings(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<BookingDetailResponse> response = getMyBookingsUseCase.execute(userId, status, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<BookingDetailResponse>> getAllBookings(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(getAllBookingsUseCase.execute(keyword, status, page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookingDetailResponse> cancelBooking(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        BookingDetailResponse response = cancelBookingUseCase.execute(id, userId);
        return ResponseEntity.ok(response);
    }
}
