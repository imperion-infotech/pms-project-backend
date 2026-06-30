/**
 * 
 */
package com.pms.auditlog.aspect;


import org.aspectj.lang.ProceedingJoinPoint;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */

@Aspect
@Component
@Slf4j
public class ApiLoggingAspect {

	@Around("within(@org.springframework.web.bind.annotation.RestController *)")
	public Object logApi(ProceedingJoinPoint joinPoint) throws Throwable {

	    ServletRequestAttributes attr =
	            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

	    if (attr != null) {
	        log.info("URI    : {}", attr.getRequest().getRequestURI());
	        log.info("Method : {}", attr.getRequest().getMethod());
	    }

	    long start = System.currentTimeMillis();

	    try {
	        return joinPoint.proceed();
	    } finally {
	        log.info("{} executed in {} ms",
	                joinPoint.getSignature().toShortString(),
	                System.currentTimeMillis() - start);
	    }
	}
}