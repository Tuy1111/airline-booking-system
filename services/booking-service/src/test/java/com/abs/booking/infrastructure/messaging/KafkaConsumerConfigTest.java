package com.abs.booking.infrastructure.messaging;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class KafkaConsumerConfigTest {

    @Test
    void deserializesPaymentEventWithoutTypeHeaders() {
        KafkaConsumerConfig config = new KafkaConsumerConfig();
        ReflectionTestUtils.setField(config, "bootstrap", "localhost:9092");
        ReflectionTestUtils.setField(config, "groupId", "test-group");

        JsonDeserializer<Object> deserializer = new JsonDeserializer<>();
        deserializer.configure(config.consumerFactory().getConfigurationProperties(), false);

        Object value = deserializer.deserialize(
                "payment.completed",
                "{\"bookingId\":85,\"paymentCode\":\"PAY-1\"}".getBytes(StandardCharsets.UTF_8));

        Map<?, ?> payload = assertInstanceOf(Map.class, value);
        assertEquals(85, payload.get("bookingId"));
        assertEquals("PAY-1", payload.get("paymentCode"));
        deserializer.close();
    }
}
