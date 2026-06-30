/**
 * 
 */
package com.pms.document.entity;

/**
 * 
 */
public enum DocumentTypeEnum {
	
	Credit_Card(1),
	Driver_License(2),
	Passport(3),
	State_ID(4);
	
	 
	    private final int description;

	    DocumentTypeEnum(int i) {
	        this.description = i;
	    }

	    public int getDescription() {
	        return description;
	    }
}
