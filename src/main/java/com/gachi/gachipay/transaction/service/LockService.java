package com.gachi.gachipay.transaction.service;

import com.gachi.gachipay.common.exception.AccountException;
import com.gachi.gachipay.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class LockService {
    private final RedissonClient redissonClient;

    /**
     * Lock 취득
     * lock 취득을 위해 최대 1초 대기
     * lock 최득 후 최대 10초의 시간이 지나면 Lock 자동 해제
     */
    public String lock(String accountNumber) {
        RLock lock = redissonClient.getLock(getLockKey(accountNumber));
        log.debug("Trying lock for accountNumber : {}", accountNumber);

        try {
            boolean isLock = lock.tryLock(1, 10, TimeUnit.SECONDS);
            if (!isLock) {
                log.error("=====Lock acquisition failed=====");
                throw new AccountException(ErrorCode.ACCOUNT_TRANSACTION_LOCK);
            }
        } catch (AccountException e) {
            throw e;
        } catch (Exception e) {
            log.error("Redis lock failed", e);
        }

        return "Lock Success";
    }

    /**
     * Lock 해지
     */
    public void unlock(String accountNumber) {
        log.debug("Unlock for accountNumber : {} ", accountNumber);
        redissonClient.getLock(getLockKey(accountNumber)).unlock();
    }

    private String getLockKey(String accountNumber) {
        return "ACLK:" + accountNumber;
    }
}
