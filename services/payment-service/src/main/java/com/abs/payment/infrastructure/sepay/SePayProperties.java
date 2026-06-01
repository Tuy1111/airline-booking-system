package com.abs.payment.infrastructure.sepay;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.sepay")
public class SePayProperties {
    private String apiKey;
    private String bankAccount;
    private String bankCode;
    private String accountHolder;
    private String qrBase;
    private String qrTemplate = "compact";
    private int    expiresMinutes = 15;
}
