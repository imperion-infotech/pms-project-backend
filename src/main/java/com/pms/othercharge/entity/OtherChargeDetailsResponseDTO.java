/**
 * 
 */
package com.pms.othercharge.entity;

import com.pms.guestdetails.GuestDetails;

import lombok.Builder;
import lombok.Data;

/**
 * 
 */

@Data
@Builder
public class OtherChargeDetailsResponseDTO {
	
	private Long id;
	
	private Double totalCharges;

	private Double amount;
	
	private String remark;
	
	private Boolean displayOnFolio;
	
	private Long guestDetailsId;
	
	private Boolean isRefund;
	
	private Double refundAmount;
	
	private String refundType;
	
	private String transactionId;
	
	private String refundAccountNo;
	
	private Boolean isDeleted;
	
	private Boolean isActive;
	
	
	
	public OtherChargeDetailsResponseDTO() {
		super();
	}



	public OtherChargeDetailsResponseDTO(Long id, Double totalCharges, Double amount, String remark,
			Boolean displayOnFolio, Long guestDetailsId, Boolean isRefund, Double refundAmount, String refundType,
			String transactionId, String refundAccountNo, Boolean isDeleted, Boolean isActive) {
		super();
		this.id = id;
		this.totalCharges = totalCharges;
		this.amount = amount;
		this.remark = remark;
		this.displayOnFolio = displayOnFolio;
		this.guestDetailsId = guestDetailsId;
		this.isRefund = isRefund;
		this.refundAmount = refundAmount;
		this.refundType = refundType;
		this.transactionId = transactionId;
		this.refundAccountNo = refundAccountNo;
		this.isDeleted = isDeleted;
		this.isActive = isActive;
	}

}
