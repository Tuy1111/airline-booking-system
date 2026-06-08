package com.abs.booking.domain.aggregate;

import com.abs.booking.domain.vo.BookingStatus;
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
}
