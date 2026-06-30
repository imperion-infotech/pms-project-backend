/**
 * 
 */
package com.pms.exception;

/**
 * 
 */
//Custom exception for resource not found
public class ResourceNotFoundException extends RuntimeException{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 public ResourceNotFoundException(String message) {
	        super(message);
	    }
}
