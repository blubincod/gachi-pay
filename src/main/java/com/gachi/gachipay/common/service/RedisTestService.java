package com.gachi.gachipay.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisTestService {
//    private final RedissonClient redissonClient;

//    /**
//     * Lock 받급
//     * 최대 3초의 시간이 지나면 Lock 해제
//     */
//    public String getLock() {
//        System.out.println("LOCKKKK");
//        RLock lock = redissonClient.getLock("sampleLock");
//
//        try {
//            boolean isLock = lock.tryLock(10, 3, TimeUnit.SECONDS);
//            if (!isLock) {
//                log.error("=====Lock acquisition failed=====");
//                return "Lock failed";
//            }
//        } catch (Exception e) {
//            log.error("Redis lock failed");
//        }
//
//        return "Lock Success";
//    }
}
