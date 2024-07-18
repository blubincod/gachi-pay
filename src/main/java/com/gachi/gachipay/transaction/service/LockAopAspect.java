package com.gachi.gachipay.transaction.service;

import com.gachi.gachipay.common.aop.AccountLockIdInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class LockAopAspect {
    private final LockService lockService;

    /**
     * lock 취득과 해제
     *
     * @Around: before와 After에 원하는 동작을 넣어준다.
     * 1. Before - lock 취득 시도
     * 2. @AccountLock 이 붙은 메서드 실행
     * 3. After - lock 해제
     */
    @Around("@annotation(com.gachi.gachipay.common.aop.AccountLock) && args(request)")
    public Object aroundMethod(
            ProceedingJoinPoint pjp,
            AccountLockIdInterface request
    ) throws Throwable {
        // lock 취득 시도
        lockService.lock(request.getAccountNumber());
        try {
            return pjp.proceed(); //
        } finally {
            // lock 해제
            lockService.unlock(request.getAccountNumber());
        }
    }
}
