package com.abs.booking.application;

import com.abs.booking.application.dto.BookingResponse;
import com.abs.booking.application.dto.FlightDetailResult;
import com.abs.booking.infrastructure.feign.FlightSearchFeignClient;
import com.abs.booking.infrastructure.feign.dto.FlightDetailResponse;
import com.abs.booking.infrastructure.feign.dto.FlightResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UseCase: gọi flight-search-service qua OpenFeign.
 * Không dùng DB, chỉ forward data giả từ flight-search-service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingUseCase {

    private final FlightSearchFeignClient flightSearchFeignClient;

    /**
     * Tìm chuyến bay → gọi Feign → trả kết quả về cho client.
     */
    public BookingResponse searchAndBook(String from, String to, String date) {
        log.info(">>> [BookingService] Gọi flight-search-service qua OpenFeign: from={}, to={}, date={}", from, to, date);

        // Bước 1: Gọi flight-search-service để tìm chuyến bay
        List<FlightResponse> flights = flightSearchFeignClient.searchFlights(from, to, date);

        log.info(">>> [BookingService] Nhận được {} chuyến bay từ flight-search-service", flights.size());

        if (flights.isEmpty()) {
            return new BookingResponse(
                    "NO_FLIGHTS", null,
                    "Không tìm thấy chuyến bay phù hợp", null
            );
        }

        // Bước 2: Lấy chuyến bay đầu tiên (giả lập chọn chuyến)
        FlightResponse selectedFlight = flights.get(0);

        // Bước 3: Trả response giả (không lưu DB)
        return new BookingResponse(
                "HELD", "BK-DEMO-001",
                "Giữ chỗ thành công", selectedFlight
        );
    }

    /**
     * Lấy chi tiết chuyến bay → gọi Feign → trả kết quả.
     */
    public FlightDetailResult getFlightDetail(Long flightId) {
        log.info(">>> [BookingService] Gọi flight-search-service lấy chi tiết flightId={}", flightId);

        FlightDetailResponse flightDetail = flightSearchFeignClient.getFlightById(flightId);

        log.info(">>> [BookingService] Nhận được chi tiết chuyến bay: {}", flightDetail.flightNo());

        return new FlightDetailResult(
                "booking-service (via OpenFeign)", flightDetail
        );
    }
}
