package com.abs.booking.api;

import com.abs.booking.application.BookingUseCase;
import com.abs.booking.application.dto.BookingResponse;
import com.abs.booking.application.dto.FlightDetailResult;
import com.abs.booking.application.dto.SearchFlightRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller - Entry point cho client gọi vào booking-service.
 *
 * Flow: Client → BookingInfoController (API) → BookingUseCase → OpenFeign → FlightInfoController
 */
@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingInfoController {

    private final BookingUseCase bookingUseCase;

    /**
     * POST /api/v1/bookings/search-and-hold
     *
     * Client gọi booking-service → booking-service dùng OpenFeign gọi flight-search-service
     *
     * Request body:
     * {
     *   "from": "HAN",
     *   "to": "SGN",
     *   "date": "2026-06-20"
     * }
     */
    @PostMapping("/search-and-hold")
    public ResponseEntity<BookingResponse> searchAndHold(@RequestBody SearchFlightRequest request) {
        BookingResponse result = bookingUseCase.searchAndBook(
                request.from(), request.to(), request.date()
        );
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/v1/bookings/flight/{flightId}
     *
     * Gọi booking-service → OpenFeign → flight-search-service lấy chi tiết chuyến bay.
     */
    @GetMapping("/flight/{flightId}")
    public ResponseEntity<FlightDetailResult> getFlightDetail(@PathVariable("flightId") Long flightId) {
        FlightDetailResult result = bookingUseCase.getFlightDetail(flightId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/ping")
    public Boolean check() {
        return true;
    }
}
