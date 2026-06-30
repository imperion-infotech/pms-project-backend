/**
 * 
 */
package com.pms.util;

import java.util.UUID;

/**
 * 
 */
public class TraceIdGenerator  {
	
	 public static String generateRequestTraceId() {
	        return "REQ-" + UUID.randomUUID().toString().substring(0, 8);
	    }

	    public static String generateBusinessTraceId() {
	        return "BUS-" + UUID.randomUUID().toString().substring(0, 8);
	    }

}
