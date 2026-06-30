/**
 * 
 */
package com.pms.tax.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 
 */
@Data
public class GuestTaxTransactionResponse {
	
	

	    private Long id;

	    private Long bookingId;

	    private String taxName;

	    private Double taxRate;

	    private BigDecimal taxableAmount;

	    private BigDecimal taxAmount;
	    			
	    private Long taxMasterId;
}


