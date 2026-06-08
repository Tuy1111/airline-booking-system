package com.abs.template.domain.aggregate;

import com.abs.template.domain.event.BaggageCheckedIn;
import com.abs.template.domain.event.BaggageOverweightFlagged;
import com.abs.template.domain.exception.BaggageStateException;
import com.abs.template.domain.vo.BaggageId;
import com.abs.template.domain.vo.BaggageStatus;
import com.abs.template.domain.vo.Weight;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit test THUẦN cho aggregate — không Spring context, không DB, chạy mili-giây.
 * Đây chính là phần thưởng của domain tách khỏi hạ tầng: nghiệp vụ kiểm thử trực tiếp.
 */
class BaggageAggregateTest {

    private static final Weight ALLOWANCE = Weight.ofKg(23);

    @Test
    void checkIn_trongHanMuc_thiCHECKED_IN_vaPhatEvent() {
        var bag = BaggageAggregate.checkIn(BaggageId.newId(), "BK-1", Weight.ofKg(20), ALLOWANCE);

        assertThat(bag.status()).isEqualTo(BaggageStatus.CHECKED_IN);
        assertThat(bag.pullDomainEvents()).singleElement().isInstanceOf(BaggageCheckedIn.class);
    }

    @Test
    void checkIn_vuotHanMuc_thiOVERWEIGHT_HELD_vaPhatEvent() {
        var bag = BaggageAggregate.checkIn(BaggageId.newId(), "BK-1", Weight.ofKg(30), ALLOWANCE);

        assertThat(bag.status()).isEqualTo(BaggageStatus.OVERWEIGHT_HELD);
        assertThat(bag.pullDomainEvents()).singleElement().isInstanceOf(BaggageOverweightFlagged.class);
    }

    @Test
    void pullDomainEvents_chiTraVeMotLan() {
        var bag = BaggageAggregate.checkIn(BaggageId.newId(), "BK-1", Weight.ofKg(20), ALLOWANCE);

        assertThat(bag.pullDomainEvents()).hasSize(1);
        assertThat(bag.pullDomainEvents()).isEmpty();   // đã rút sạch
    }

    @Test
    void markLoaded_tuCHECKED_IN_thanhCong() {
        var bag = BaggageAggregate.checkIn(BaggageId.newId(), "BK-1", Weight.ofKg(20), ALLOWANCE);

        bag.markLoaded();

        assertThat(bag.status()).isEqualTo(BaggageStatus.LOADED);
    }

    @Test
    void markLoaded_khiOVERWEIGHT_HELD_thiNemLoi() {
        var bag = BaggageAggregate.checkIn(BaggageId.newId(), "BK-1", Weight.ofKg(30), ALLOWANCE);

        assertThatThrownBy(bag::markLoaded).isInstanceOf(BaggageStateException.class);
    }

    @Test
    void weight_amThiNemLoi() {
        assertThatThrownBy(() -> Weight.ofKg(-1)).isInstanceOf(IllegalArgumentException.class);
    }
}
