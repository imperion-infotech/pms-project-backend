/**
 * 
 */
package com.pms.paymentdetails.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pms.paymentdetails.enums.ApprovalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPaymentApprovalRequestDTO {

    private Long guestDetailsId;

    private Double amount;

    private String paymentMethod;

    private String remarks;

    private String approverUsername;

    private String approverPassword;

    private String approverPin;

    private ApprovalType approvalType;
    
    private LocalDateTime paymentDate;
    
    private String currencySymbol;
    
    private String receiptNo;
    
    private LocalDateTime validTill;
    
    private String cardNo;
    
    private Double totalAmount;
    
    private Boolean isRefund;
	
	private Double refundAmount;
	
	private String refundType;
	
	private String transactionId;
	
	private String refundAccountNo;
    
    
}