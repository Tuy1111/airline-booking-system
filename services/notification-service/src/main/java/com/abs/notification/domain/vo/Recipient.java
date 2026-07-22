package com.abs.notification.domain.vo;

import com.abs.notification.domain.enums.Channel;

/** Value Object: a delivery target, defined by attributes, immutable, equality by value. */
public record Recipient(Channel channel, String address) {
    public Recipient {
        if (channel == null) throw new IllegalArgumentException("Recipient: channel required");
        if (address == null || address.isBlank()) throw new IllegalArgumentException("Recipient: address required");
    }
    public static Recipient of(Channel channel, String address) { return new Recipient(channel, address); }
}
