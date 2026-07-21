package com.abs.flightsearch.domain.aggregate;

import com.abs.flightsearch.domain.vo.SeatClass;
import com.abs.flightsearch.domain.vo.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSeatAggregate {
    private Long flightId;
    private String seatNo;
    private SeatClass seatClass;
    private SeatStatus status;
    private BigDecimal priceFactor;

    public static FlightSeatAggregate create(Long flightId, String seatNo, SeatClass seatClass, BigDecimal priceFactor) {
        if (flightId == null) {
            throw new IllegalArgumentException("Flight ID không được trống");
        }
        if (seatNo == null || seatNo.isBlank()) {
            throw new IllegalArgumentException("Số ghế không được trống");
        }
        if (seatClass == null) {
            throw new IllegalArgumentException("Hạng ghế không được trống");
        }
        return FlightSeatAggregate.builder()
                .flightId(flightId)
                .seatNo(seatNo.toUpperCase())
                .seatClass(seatClass)
                .status(SeatStatus.AVAILABLE)
                .priceFactor(priceFactor != null ? priceFactor : BigDecimal.ONE)
                .build();
    }

    public void hold() {
        if (!isAvailable()) {
            throw new IllegalStateException("Ghế không ở trạng thái khả dụng để giữ chỗ");
        }
        this.status = SeatStatus.HELD;
    }

    public void book() {
        if (this.status == SeatStatus.BOOKED) {
            throw new IllegalStateException("Ghế đã được đặt trước đó");
        }
        this.status = SeatStatus.BOOKED;
    }

    public void release() {
        this.status = SeatStatus.AVAILABLE;
    }

    public boolean isAvailable() {
        return this.status == SeatStatus.AVAILABLE;
    }

    public boolean isHeld() {
        return this.status == SeatStatus.HELD;
    }

    public boolean isBooked() {
        return this.status == SeatStatus.BOOKED;
    }
}
