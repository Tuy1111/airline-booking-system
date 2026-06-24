package com.abs.payment.application;

import com.abs.payment.application.dto.PaymentCompletedEvent;
import com.abs.payment.application.dto.PaymentFailedEvent;

public interface PaymentEventPublisher {
    void publishCompleted(PaymentCompletedEvent event);
    void publishFailed(PaymentFailedEvent event);
}
