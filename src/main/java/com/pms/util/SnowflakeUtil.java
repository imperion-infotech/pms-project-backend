/**
 * 
 */
package com.pms.util;

import org.springframework.stereotype.Component;

/**
 * 
 */
@Component
public class SnowflakeUtil {
	
	
	 private final SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 1);

	    public long generateId() {
	        return generator.nextId();
	    }
}