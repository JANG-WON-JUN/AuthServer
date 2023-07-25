package com.my.authserver.config.aop;

import com.my.authserver.config.aop.annotaion.Retry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RetryAspect {

    // 파라미터로 retry를 받는다.
    @Around("@annotation(retry)")
    @SuppressWarnings("ConstantConditions")
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        int maxReties = retry.value();
        Exception exceptionHolder = null;

        for (int retryCount = 1; retryCount <= maxReties; retryCount++) {
            try {
                return joinPoint.proceed();
            }catch (Exception e){
                exceptionHolder = e;
            }
        }

        throw exceptionHolder;
    }
}
