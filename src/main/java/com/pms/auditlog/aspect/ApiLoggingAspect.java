/**
 * 
 */
package com.pms.auditlog.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */

@Aspect
@Component
@Slf4j
public class ApiLoggingAspect {

    @Around("execution(* com.pms..controller..*(..))")
    public Object logApi(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        log.info("========== API START ==========");
        log.info("Controller : {}", joinPoint.getSignature().toShortString());

        try {

            Object result = joinPoint.proceed();

            log.info("Controller executed successfully");

            return result;

        } catch (Exception ex) {

            log.error("Exception in {}", joinPoint.getSignature().toShortString(), ex);
            throw ex;

        } finally {

            log.info("========== API END ==========");
            log.info("Time Taken : {} ms",
                    System.currentTimeMillis() - start);
        }
    }
}