package com.abs.booking.domain.aggregate;

import com.abs.booking.domain.vo.BookingStatus;
import com.abs.booking.domain.exception.InvalidBookingStateException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingAggregate {

    private Long id;
    private String bookingCode;
    private Long userId;
    private Long flightId;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private Integer baggageWeightKg;
    private BigDecimal baggageFee;
    @Builder.Default
    private String currency = "VND";
    private LocalDateTime heldAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime cancelledAt;
    private String paymentId;
    private Long version;
    private LocalDateTime createdAt;
    @Builder.Default
    private List<BookingItem> items = new ArrayList<>();

    public void addItem(BookingItem item) {
        items.add(item);
    }

    public static BookingAggregate createHold(String bookingCode, Long userId, Long flightId, BigDecimal totalAmount, Integer baggageWeightKg, BigDecimal baggageFee, int holdTtlMinutes) {
        LocalDateTime now = LocalDateTime.now();
        return BookingAggregate.builder()
                .bookingCode(bookingCode)
                .userId(userId)
                .flightId(flightId)
                .status(BookingStatus.HELD)
                .totalAmount(totalAmount)
                .baggageWeightKg(baggageWeightKg != null ? baggageWeightKg : 0)
                .baggageFee(baggageFee != null ? baggageFee : BigDecimal.ZERO)
                .currency("VND")
                .heldAt(now)
                .expiresAt(now.plusMinutes(holdTtlMinutes))
                .createdAt(now)
                .items(new ArrayList<>())
                .build();
    }


    public void confirm(String paymentId) {
        if (this.status != BookingStatus.HELD) {
            throw new InvalidBookingStateException("Cannot confirm booking in state: " + this.status);
        }
        this.status = BookingStatus.CONFIRMED;
        this.paymentId = paymentId;
        this.confirmedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status != BookingStatus.HELD && this.status != BookingStatus.CONFIRMED) {
            throw new InvalidBookingStateException("Cannot cancel booking in state: " + this.status);
        }
        this.status = BookingStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }

    public void expire() {
        if (this.status != BookingStatus.HELD) {
            throw new InvalidBookingStateException("Cannot expire booking in state: " + this.status);
        }
        this.status = BookingStatus.EXPIRED;
    }

    public boolean isExpired() {
        return this.status == BookingStatus.HELD
                && this.expiresAt != null
                && LocalDateTime.now().isAfter(this.expiresAt);
    }

    public List<String> getSeatNumbers() {
        if (this.items == null) {
            return List.of();
        }
        return this.items.stream()
                .map(BookingItem::getSeatNo)
                .toList();
    }
}
