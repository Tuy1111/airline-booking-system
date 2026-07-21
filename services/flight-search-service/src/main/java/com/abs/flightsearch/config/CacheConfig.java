package com.abs.flightsearch.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        RedisSerializer<Object> serializer = GenericJackson2JsonRedisSerializer.builder()
                .objectMapper(objectMapper.copy())
                .defaultTyping(true)
                .build();
        RedisCacheConfiguration defaults = configuration(Duration.ofSeconds(30), serializer);
        Map<String, RedisCacheConfiguration> caches = Map.of(
                "airports", configuration(Duration.ofHours(12), serializer),
                "airlines", configuration(Duration.ofHours(12), serializer),
                "routes", configuration(Duration.ofHours(12), serializer),
                "flightSearch", configuration(Duration.ofSeconds(20), serializer),
                "flightDetail", configuration(Duration.ofSeconds(20), serializer),
                "adminFlights", configuration(Duration.ofSeconds(20), serializer),
                "upcomingFlights", configuration(Duration.ofSeconds(20), serializer)
        );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaults)
                .withInitialCacheConfigurations(caches)
                .transactionAware()
                .build();
    }

    private RedisCacheConfiguration configuration(Duration ttl, RedisSerializer<Object> serializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .disableCachingNullValues()
                .computePrefixWith(cacheName -> "flight-cache:" + cacheName + "::")
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        serializer));
    }
}
