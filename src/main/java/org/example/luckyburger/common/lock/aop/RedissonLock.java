package org.example.luckyburger.common.lock.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedissonLock {
    // SpEL로 파싱할 키
    String key();

    // 락 획득을 기다릴 시간
    long waitTimeS() default 3;

    // 락을 유지할 시간
    long leaseTimeS() default 5;

}