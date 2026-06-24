package com.abs.template.domain.port.in;

import com.abs.template.domain.vo.BaggageId;

/**
 * Inbound Port (driving) — use case "ký gửi hành lý".
 *
 * <p>Là hợp đồng mà thế giới bên ngoài (REST controller, listener…) gọi vào lõi.
 * Adapter inbound phụ thuộc interface này; nó KHÔNG biết class hiện thực nào đứng sau.
 */
public interface CheckInBaggageUseCase {
    BaggageId checkIn(CheckInBaggageCommand command);
}
