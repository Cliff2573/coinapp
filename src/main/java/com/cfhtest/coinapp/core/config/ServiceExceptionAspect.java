package com.cfhtest.coinapp.core.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.cfhtest.coinapp.core.exception.BusinessException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Aspect
@Component
public class ServiceExceptionAspect {

    /**
     * 攔截所有 Service 的 public 方法
     */
    @Pointcut("execution(* com.cfhtest.coinapp.service..*(..))")
    public void serviceMethods() {}

    /**
     * 攔截 Service 的 public 方法，並記錄請求和回應的資訊
     * @param joinPoint 攔截點
     * @return 方法執行結果
     * @throws Throwable 可能拋出的例外
     */
    @Around("serviceMethods()")
    public Object handleServiceExceptions(ProceedingJoinPoint joinPoint) throws Throwable {

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        long start = System.currentTimeMillis();

        // method 執行成功
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            log.info("[Service] 成功 class={} method={} duration={}ms", className, methodName, duration);
            return result;

        } 
        // method 執行失敗
        catch (Exception e) {
            long duration = System.currentTimeMillis() - start;

            log.error("[Service] 錯誤 class={} method={} duration={}ms error={}", className, methodName, duration, e.getMessage(), e);

            throw (e instanceof BusinessException) ? e : new BusinessException(e.getMessage());
        }
    }
}