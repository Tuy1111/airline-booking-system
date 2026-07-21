package com.abs.flightsearch.domain.aggregate;

import com.abs.flightsearch.domain.vo.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightAggregate {
    private Long id;
    private String flightNo;
    private RouteAggregate route;
    private AirlineAggregate airline;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer totalSeats;
    private BigDecimal basePrice;
    private String aircraftType;
    private FlightStatus status;
    private Long version;
    private LocalDateTime createdAt;

    public static FlightAggregate create(String flightNo, RouteAggregate route, AirlineAggregate airline,
                                         LocalDateTime departureTime, LocalDateTime arrivalTime,
                                         Integer totalSeats, BigDecimal basePrice, String aircraftType) {
        if (flightNo == null || flightNo.isBlank()) {
            throw new IllegalArgumentException("Mã chuyến bay không được trống");
        }
        if (route == null) {
            throw new IllegalArgumentException("Tuyến bay không được trống");
        }
        if (airline == null) {
            throw new IllegalArgumentException("Hãng hàng không không được trống");
        }
        if (departureTime == null || arrivalTime == null) {
            throw new IllegalArgumentException("Thời gian đi và đến không được trống");
        }
        if (arrivalTime.isBefore(departureTime)) {
            throw new IllegalArgumentException("Thời gian đến phải sau thời gian đi");
        }
        if (totalSeats == null || totalSeats <= 0) {
            throw new IllegalArgumentException("Tổng số ghế phải lớn hơn 0");
        }
        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giá vé cơ bản không hợp lệ");
        }

        return FlightAggregate.builder()
                .flightNo(flightNo.trim().toUpperCase())
                .route(route)
                .airline(airline)
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .totalSeats(totalSeats)
                .basePrice(basePrice)
                .aircraftType(aircraftType != null ? aircraftType.trim() : null)
                .status(FlightStatus.SCHEDULED)
                .build();
    }

    public void updateDetails(LocalDateTime departureTime, LocalDateTime arrivalTime, BigDecimal basePrice, String aircraftType) {
        if (departureTime == null || arrivalTime == null) {
            throw new IllegalArgumentException("Thời gian đi và đến không được trống");
        }
        if (arrivalTime.isBefore(departureTime)) {
            throw new IllegalArgumentException("Thời gian đến phải sau thời gian đi");
        }
        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giá vé cơ bản không hợp lệ");
        }

        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.basePrice = basePrice;
        if (aircraftType != null) {
            this.aircraftType = aircraftType.trim();
        }
    }

    public void delay(LocalDateTime newDeparture, LocalDateTime newArrival) {
        if (newDeparture == null || newArrival == null) {
            throw new IllegalArgumentException("Thời gian đi và đến mới không được trống");
        }
        if (newArrival.isBefore(newDeparture)) {
            throw new IllegalArgumentException("Thời gian đến mới phải sau thời gian đi");
        }
        this.departureTime = newDeparture;
        this.arrivalTime = newArrival;
        this.status = FlightStatus.DELAYED;
    }

    public void cancel() {
        this.status = FlightStatus.CANCELLED;
    }

    public void depart() {
        if (this.status == FlightStatus.CANCELLED) {
            throw new IllegalStateException("Không thể cất cánh chuyến bay đã hủy");
        }
        this.status = FlightStatus.DEPARTED;
    }

    public void schedule() {
        this.status = FlightStatus.SCHEDULED;
    }

    public Duration calculateDuration() {
        if (this.departureTime == null || this.arrivalTime == null) {
            return Duration.ZERO;
        }
        return Duration.between(this.departureTime, this.arrivalTime);
    }

    public BigDecimal calculateCurrentPrice(SeatInventoryAggregate inv) {
        if (inv == null || inv.getTotal() <= 0) {
            return this.basePrice;
        }
        double occupancy = inv.calculateOccupancy();
        BigDecimal multiplier = BigDecimal.valueOf(1.0 + 0.1 * occupancy);
        return this.basePrice.multiply(multiplier).setScale(0, RoundingMode.CEILING);
    }

    public BigDecimal calculateSeatPrice(FlightSeatAggregate seat, SeatInventoryAggregate inv) {
        BigDecimal base = this.basePrice;
        BigDecimal factor = seat.getPriceFactor() != null ? seat.getPriceFactor() : BigDecimal.ONE;
        
        double occupancy = 0.0;
        if (inv != null) {
            occupancy = inv.calculateOccupancy();
        }
        
        BigDecimal dynamicFactor = BigDecimal.valueOf(1.0 + 0.1 * occupancy);
        return base.multiply(factor).multiply(dynamicFactor).setScale(0, RoundingMode.CEILING);
    }

    public boolean isCancelled() {
        return this.status == FlightStatus.CANCELLED;
    }

    public boolean isDeparted() {
        return this.status == FlightStatus.DEPARTED;
    }

    public boolean isDelayed() {
        return this.status == FlightStatus.DELAYED;
    }

    public boolean isBookableAt(LocalDateTime now) {
        return this.status == FlightStatus.SCHEDULED
                && this.departureTime != null
                && this.departureTime.isAfter(now);
    }

    public void ensureBookableAt(LocalDateTime now) {
        if (!isBookableAt(now)) {
            throw new IllegalStateException("Chỉ chuyến bay đúng lịch và chưa khởi hành mới được đặt chỗ");
        }
    }
}
