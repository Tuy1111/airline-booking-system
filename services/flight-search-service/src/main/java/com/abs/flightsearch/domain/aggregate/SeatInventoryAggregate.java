package com.abs.flightsearch.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatInventoryAggregate {
    private Long flightId;
    private Integer total;
    private Integer available;
    private Integer held;
    private Integer booked;
    private Long version;

    public static SeatInventoryAggregate create(Long flightId, Integer total) {
        if (flightId == null) {
            throw new IllegalArgumentException("Flight ID không được trống");
        }
        if (total == null || total <= 0) {
            throw new IllegalArgumentException("Tổng số ghế phải lớn hơn 0");
        }
        return SeatInventoryAggregate.builder()
                .flightId(flightId)
                .total(total)
                .available(total)
                .held(0)
                .booked(0)
                .build();
    }

    public void updateTotalSeats(Integer newTotal) {
        if (newTotal == null || newTotal <= 0) {
            throw new IllegalArgumentException("Tổng số ghế mới phải lớn hơn 0");
        }
        int diff = newTotal - this.total;
        this.total = newTotal;
        this.available += diff;
        if (this.available < 0) {
            throw new IllegalStateException("Không thể giảm tổng số ghế vì số ghế trống sẽ âm");
        }
    }

    public double calculateOccupancy() {
        if (this.total == null || this.total <= 0) {
            return 0.0;
        }
        return (double) this.booked / this.total;
    }

    public boolean hasAvailableSeats(int passengers) {
        return this.available >= passengers;
    }

    public void holdSeats(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Số lượng ghế giữ phải lớn hơn 0");
        }
        if (this.available < count) {
            throw new IllegalStateException("Không đủ ghế trống để giữ chỗ");
        }
        this.available -= count;
        this.held += count;
    }

    public void confirmSeats(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Số lượng ghế xác nhận phải lớn hơn 0");
        }
        if (this.held < count) {
            throw new IllegalStateException("Số lượng ghế đang giữ không đủ để xác nhận");
        }
        this.held -= count;
        this.booked += count;
    }

    public void releaseSeats(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Số lượng ghế giải phóng phải lớn hơn 0");
        }
        if (this.held < count) {
            throw new IllegalStateException("Số lượng ghế đang giữ không đủ để giải phóng");
        }
        this.held -= count;
        this.available += count;
    }

    public void cancelSeats(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Số lượng ghế hủy phải lớn hơn 0");
        }
        if (this.booked < count) {
            throw new IllegalStateException("Số lượng ghế đã bán không đủ để hủy");
        }
        this.booked -= count;
        this.available += count;
    }
}
