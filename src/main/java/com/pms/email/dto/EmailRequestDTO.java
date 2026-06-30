/**
 * 
 */
package com.pms.email.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
public class EmailRequestDTO {
	
	private String to;
	
	private String subject;
	
	private String text;
	
	 private List<String> cc;

	    private List<String> bcc;

}
