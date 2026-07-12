package com.abs.booking.infrastructure.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlightSearchClient {

    private final RestTemplate restTemplate;

    @Value("${app.services.flight-search.url:http://localhost:8081}")
    private String baseUrl;

    @SuppressWarnings("unchecked")
    public Map<String, Object> checkSeat(Long flightId, String seatNo) {
        try {
            String url = baseUrl + "/api/v1/flights/" + flightId + "/seats/" + seatNo;
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            log.error("Failed to check seat availability for flightId={}, seatNo={}: {}", flightId, seatNo, e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getFlightDetails(Long flightId) {
        try {
            String url = baseUrl + "/api/v1/flights/" + flightId;
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            log.error("Failed to fetch flight details for flightId={}: {}", flightId, e.getMessage());
            return null;
        }
    }

    public boolean holdSeat(Long flightId, String seatNo) {
        try {
            String url = baseUrl + "/api/v1/flights/" + flightId + "/seats/" + seatNo + "/hold";
            restTemplate.put(url, null);
            return true;
        } catch (Exception e) {
            log.error("Failed to hold seat for flightId={}, seatNo={}: {}", flightId, seatNo, e.getMessage());
            return false;
        }
    }

    public boolean bookSeat(Long flightId, String seatNo) {
        try {
            String url = baseUrl + "/api/v1/flights/" + flightId + "/seats/" + seatNo + "/book";
            restTemplate.put(url, null);
            return true;
        } catch (Exception e) {
            log.error("Failed to book seat for flightId={}, seatNo={}: {}", flightId, seatNo, e.getMessage());
            return false;
        }
    }

    public boolean releaseSeat(Long flightId, String seatNo) {
        try {
            String url = baseUrl + "/api/v1/flights/" + flightId + "/seats/" + seatNo + "/release";
            restTemplate.put(url, null);
            return true;
        } catch (Exception e) {
            log.error("Failed to release seat for flightId={}, seatNo={}: {}", flightId, seatNo, e.getMessage());
            return false;
        }
    }
}
