package com.gachi.gachipay.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisTestService {
    private final RedissonClient redissonClient;

    /**
     * Lock 취득 테스트
     * 최대 3초의 시간이 지나면 Lock 해제
     */
    public String getLock() {
        RLock lock = redissonClient.getLock("sampleLock");

        try {
            boolean isLock = lock.tryLock(1, 3, TimeUnit.SECONDS);
            if (!isLock) {
                log.error("=====Lock acquisition failed=====");
                return "Lock failed";
            }
        } catch (Exception e) {
            log.error("Redis lock failed");
        }

        return "Lock Success";
    }
}
