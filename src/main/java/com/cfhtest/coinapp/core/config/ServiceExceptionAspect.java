package com.cfhtest.coinapp.core.config;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.cfhtest.coinapp.core.exception.BusinessException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Aspect
@Component
public class ServiceExceptionAspect {

    @Pointcut("execution(* com.cfhtest.coinapp.service..*(..))")
    public void serviceMethods() {}

    @Around("serviceMethods()")
    public Object handleServiceExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            log.info("[Service Success] traceId={} class={} method={} args={} duration={}ms",
                    MDC.get("traceId"), className, methodName, Arrays.toString(args), duration);

            return result;

        } catch (IllegalArgumentException e) {
            log.warn("[Service InvalidArg] traceId={} class={} method={} args={} error={}",
                    MDC.get("traceId"), className, methodName, Arrays.toString(args), e.getMessage());
            throw e;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;

            log.error("[Service Error] traceId={} class={} method={} args={} duration={}ms",
                    MDC.get("traceId"), className, methodName, Arrays.toString(args), duration, e);

            throw new BusinessException(e.getMessage());
        }
    }
}