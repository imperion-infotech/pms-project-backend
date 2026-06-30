/**
 * 
 */
package com.pms.enumerated.dto;

/**
 * 
 */
public class EnumResponse {
	 private String name;
	    private int value;

	    public EnumResponse(String name, int i) {
	        this.name = name;
	        this.value = i;
	    }

	    public String getName() { return name; }
	    public int getValue() { return value; }
	}
