/**
 * 
 */
package com.pms.auditlog.context;

import org.slf4j.MDC;

/**
 * 
 */
public class BusinessTraceContext {
    private static final ThreadLocal<String> holder = new ThreadLocal<>();

    public static void set(String businessTraceId) {
        holder.set(businessTraceId);
    }

    public static String get() {
        return holder.get();
    }

    public static void clear() {
        holder.remove();
    }
}