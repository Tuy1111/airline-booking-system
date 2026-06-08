package com.abs.payment.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAggregate {
    private Long id;
    private Long paymentId;
    private String gatewayTxnId;
    private String gatewayResponse;
    private String status;
    private LocalDateTime createdAt;
}
