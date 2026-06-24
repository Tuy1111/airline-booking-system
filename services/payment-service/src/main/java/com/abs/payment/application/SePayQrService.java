package com.abs.payment.application;

import com.abs.payment.infrastructure.sepay.SePayProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SePayQrService {

    private final SePayProperties props;

    /** Mã đưa vào nội dung chuyển khoản. Phải UNIQUE để đối soát. */
    public String newTransferCode(Long bookingId) {
        return "ABS" + bookingId + RandomStringUtils.randomAlphanumeric(5).toUpperCase();
    }

    /** Build VietQR URL theo chuẩn sepay.vn. Hiển thị ảnh QR cho client. */
    public String buildQrUrl(String transferCode, BigDecimal amount) {
        return UriComponentsBuilder.fromHttpUrl(props.getQrBase())
                .queryParam("acc",      props.getBankAccount())
                .queryParam("bank",     props.getBankCode())
                .queryParam("amount",   amount.toPlainString())
                .queryParam("des",      transferCode)
                .queryParam("template", props.getQrTemplate())
                .build()
                .toUriString();
    }
}
