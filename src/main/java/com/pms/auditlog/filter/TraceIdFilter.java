/**
 * 
 */
package com.pms.auditlog.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pms.auditlog.context.BusinessTraceContext;
import com.pms.auditlog.context.RequestTraceContext;
import com.pms.util.TraceIdGenerator;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TraceIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TraceIdFilter.class);

    private static final String BUSINESS_TRACE_HEADER = "X-Business-Trace-Id";
    private static final String REQUEST_TRACE_HEADER = "X-Request-Trace-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestTraceId = TraceIdGenerator.generateRequestTraceId();
        String businessTraceId = request.getHeader(BUSINESS_TRACE_HEADER);

        if (businessTraceId == null || businessTraceId.isBlank()) {
            businessTraceId = TraceIdGenerator.generateBusinessTraceId();
        }

        RequestTraceContext.set(requestTraceId);
        BusinessTraceContext.set(businessTraceId);

        MDC.put("requestTraceId", requestTraceId);
        MDC.put("businessTraceId", businessTraceId);

        response.setHeader(REQUEST_TRACE_HEADER, requestTraceId);
        response.setHeader(BUSINESS_TRACE_HEADER, businessTraceId);

        try {
            log.info("TraceIdFilter executed");
            filterChain.doFilter(request, response);
        } finally {
            RequestTraceContext.clear();
            BusinessTraceContext.clear();

            MDC.remove("requestTraceId");
            MDC.remove("businessTraceId");
        }
    }
}