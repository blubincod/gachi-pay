package com.gachi.gachipay.transaction.aop;

import java.lang.annotation.*;

/**
 * 중복 거래 방지를 위한 Lock
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AccountLock {
    long tryLockTime() default 5000L;
}
