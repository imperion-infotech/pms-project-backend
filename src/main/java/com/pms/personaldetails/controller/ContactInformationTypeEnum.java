/**
 * 
 */
package com.pms.personaldetails.controller;

/**
 * 
 */
public enum ContactInformationTypeEnum {
	
	HOME(0),
	OFFICE(1),
	OTHER(2);

	private final int description;

	ContactInformationTypeEnum(int i) {
        this.description = i;
    }

    public int getDescription() {
        return description;
    }
}
