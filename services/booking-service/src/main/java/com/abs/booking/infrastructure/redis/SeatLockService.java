package com.abs.booking.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatLockService {

    private final StringRedisTemplate stringRedisTemplate;

    public boolean acquireLock(Long flightId, String seatNo, Long userId, Duration ttl) {
        String key = buildRedisKey(flightId, seatNo);
        Boolean acquired = stringRedisTemplate.opsForValue().setIfAbsent(key, userId.toString(), ttl);
        boolean result = Boolean.TRUE.equals(acquired);
        if (result) {
            log.info("Acquired Redis lock for key: {} by userId: {}", key, userId);
        } else {
            log.warn("Failed to acquire Redis lock for key: {}, already locked", key);
        }
        return result;
    }

    public void releaseLock(Long flightId, String seatNo) {
        String key = buildRedisKey(flightId, seatNo);
        stringRedisTemplate.delete(key);
        log.info("Released Redis lock for key: {}", key);
    }

    private String buildRedisKey(Long flightId, String seatNo) {
        return "seat:" + flightId + ":" + seatNo;
    }
}
