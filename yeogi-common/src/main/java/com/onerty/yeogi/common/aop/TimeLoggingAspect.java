package com.onerty.yeogi.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimeLoggingAspect {

    @Around("@annotation(com.onerty.yeogi.common.aop.Timed)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();  // 실제 메서드 실행

        long end = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        log.info("️⏰ {} 실행 시간: {} ms", methodName, (end - start));

        return result;
    }
}
