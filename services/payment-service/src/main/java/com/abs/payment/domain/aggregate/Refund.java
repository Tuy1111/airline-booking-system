package com.abs.payment.domain.aggregate;

import com.abs.payment.domain.vo.Money;
import com.abs.payment.domain.enums.RefundStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Refund {
    private Long id;
    private Long paymentId;
    private Money amount;
    private String reason;
    private RefundStatus status;
    private LocalDateTime createdAt;
}
