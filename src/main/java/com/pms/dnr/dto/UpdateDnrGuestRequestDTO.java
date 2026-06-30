/**
 * 
 */
package com.pms.dnr.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
public class UpdateDnrGuestRequestDTO {
	
	private String reason;

    private String remarks;

    private Boolean dnrStatus;
	

}
