package com.abs.template.domain.aggregate;

import com.abs.template.domain.event.BaggageCheckedIn;
import com.abs.template.domain.event.BaggageOverweightFlagged;
import com.abs.template.domain.event.DomainEvent;
import com.abs.template.domain.exception.BaggageStateException;
import com.abs.template.domain.vo.BaggageId;
import com.abs.template.domain.vo.BaggageStatus;
import com.abs.template.domain.vo.Weight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate Root — kiện hành lý ký gửi.
 *
 * <p><b>Đây là nơi nghiệp vụ sống.</b> Khác với entity "anemic" (chỉ getter/setter),
 * aggregate này:
 * <ul>
 *   <li>Bảo vệ invariant: chỉ tạo qua factory {@link #checkIn}, trạng thái không bị set tuỳ tiện.</li>
 *   <li>Chứa hành vi: {@link #checkIn}, {@link #markLoaded}, {@link #remove}.</li>
 *   <li>Tự ghi nhận Domain Event khi có sự kiện nghiệp vụ ý nghĩa.</li>
 * </ul>
 *
 * <p>Tuyệt đối KHÔNG phụ thuộc Spring/JPA — đó là lý do nó test được bằng unit test
 * thuần, không cần context. Việc ánh xạ sang bảng DB là việc của adapter persistence.
 */
public class BaggageAggregate {

    private final BaggageId id;
    private final String bookingRef;
    private final Weight weight;
    private BaggageStatus status;

    /** Domain event tích luỹ trong vòng đời 1 transaction, được adapter rút ra sau khi save. */
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private BaggageAggregate(BaggageId id, String bookingRef, Weight weight, BaggageStatus status) {
        this.id = id;
        this.bookingRef = bookingRef;
        this.weight = weight;
        this.status = status;
    }

    /**
     * Factory nghiệp vụ — ký gửi một kiện hành lý mới.
     * Tự quyết định trạng thái theo hạn mức và phát Domain Event tương ứng.
     */
    public static BaggageAggregate checkIn(BaggageId id, String bookingRef, Weight weight, Weight allowance) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(weight, "weight");
        Objects.requireNonNull(allowance, "allowance");
        if (bookingRef == null || bookingRef.isBlank()) {
            throw new IllegalArgumentException("bookingRef must not be blank");
        }

        boolean overweight = weight.exceeds(allowance);
        var baggage = new BaggageAggregate(
                id, bookingRef, weight,
                overweight ? BaggageStatus.OVERWEIGHT_HELD : BaggageStatus.CHECKED_IN);

        if (overweight) {
            baggage.record(new BaggageOverweightFlagged(id, bookingRef, weight, allowance));
        } else {
            baggage.record(new BaggageCheckedIn(id, bookingRef, weight));
        }
        return baggage;
    }

    /**
     * Tái tạo aggregate từ dữ liệu đã lưu (gọi bởi persistence adapter).
     * KHÔNG phát event — đây là việc khôi phục, không phải sự kiện nghiệp vụ mới.
     */
    public static BaggageAggregate rehydrate(
            BaggageId id,
            String bookingRef,
            Weight weight,
            BaggageStatus status
    ) {
        return new BaggageAggregate(id, bookingRef, weight, status);
    }

    /** Xếp hành lý lên tàu bay. Chỉ hợp lệ khi đang CHECKED_IN. */
    public void markLoaded() {
        if (status != BaggageStatus.CHECKED_IN) {
            throw new BaggageStateException(
                    "Chỉ load được hành lý đang CHECKED_IN, hiện tại=" + status);
        }
        this.status = BaggageStatus.LOADED;
    }

    /** Gỡ hành lý khỏi chuyến (không thể gỡ khi đã LOADED). */
    public void remove() {
        if (status == BaggageStatus.LOADED) {
            throw new BaggageStateException("Không thể gỡ hành lý đã LOADED");
        }
        this.status = BaggageStatus.REMOVED;
    }

    private void record(DomainEvent event) {
        domainEvents.add(event);
    }

    /** Rút sạch event đã tích luỹ (đọc-rồi-xoá) để adapter publish. */
    public List<DomainEvent> pullDomainEvents() {
        var copy = List.copyOf(domainEvents);
        domainEvents.clear();
        return copy;
    }

    public List<DomainEvent> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public BaggageId id()           { return id; }
    public String bookingRef()      { return bookingRef; }
    public Weight weight()          { return weight; }
    public BaggageStatus status()   { return status; }
}
