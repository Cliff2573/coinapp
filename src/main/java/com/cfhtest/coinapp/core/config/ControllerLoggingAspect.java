package com.cfhtest.coinapp.core.config;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {
    
    /**
     * 攔截所有 Controller 的 public 方法
     */
    @Pointcut("execution(public * com.cfhtest.coinapp.controller..*(..))")
    public void controllerMethods() {
    }

    /**
     * 攔截 Controller 的 public 方法，並記錄請求和回應的資訊
     * @param joinPoint 攔截點
     * @return 方法執行結果
     * @throws Throwable 可能拋出的例外
     */
    @Around("controllerMethods()")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        long start = System.currentTimeMillis();

        // input
        log.info("[Controller] 請求方法={}，輸入參數={}", method, Arrays.toString(args));

        // output
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            log.info("[Controller] 回應方法={}，結果={}，耗時={}ms", method, result, duration);
            return result;
        } 
        // error 拋給 GlobalExceptionHandler 處理就好
        catch (Exception e) {
            throw e;
        }
    }

}
