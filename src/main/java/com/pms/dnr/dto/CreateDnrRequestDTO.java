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
public class CreateDnrRequestDTO {
	
	private Long personalDetailsId;

    private String reason;

    private String remarks;

}
