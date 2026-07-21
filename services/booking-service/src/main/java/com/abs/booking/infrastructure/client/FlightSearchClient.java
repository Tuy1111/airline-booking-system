package com.abs.booking.infrastructure.client;

import com.abs.booking.application.dto.FlightDetailResponse;
import com.abs.booking.application.dto.SeatInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlightSearchClient {

    private final FlightSearchFeignClient flightSearchFeignClient;

    public SeatInfoResponse checkSeat(Long flightId, String seatNo) {
        try {
            return flightSearchFeignClient.checkSeat(flightId, seatNo);
        } catch (Exception e) {
            log.error("Failed to check seat availability for flightId={}, seatNo={}: {}", flightId, seatNo, e.getMessage());
            return null;
        }
    }

    public FlightDetailResponse getFlightDetails(Long flightId) {
        try {
            return flightSearchFeignClient.getFlightDetails(flightId);
        } catch (Exception e) {
            log.error("Failed to fetch flight details for flightId={}: {}", flightId, e.getMessage());
            return null;
        }
    }

    public boolean holdSeat(Long flightId, String seatNo) {
        try {
            flightSearchFeignClient.holdSeat(flightId, seatNo);
            return true;
        } catch (Exception e) {
            log.error("Failed to hold seat for flightId={}, seatNo={}: {}", flightId, seatNo, e.getMessage());
            return false;
        }
    }

    public boolean bookSeat(Long flightId, String seatNo) {
        try {
            flightSearchFeignClient.bookSeat(flightId, seatNo);
            return true;
        } catch (Exception e) {
            log.error("Failed to book seat for flightId={}, seatNo={}: {}", flightId, seatNo, e.getMessage());
            return false;
        }
    }

    public boolean releaseSeat(Long flightId, String seatNo) {
        try {
            flightSearchFeignClient.releaseSeat(flightId, seatNo);
            return true;
        } catch (Exception e) {
            log.error("Failed to release seat for flightId={}, seatNo={}: {}", flightId, seatNo, e.getMessage());
            return false;
        }
    }
}
