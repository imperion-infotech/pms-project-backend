/**
 * 
 */
package com.pms.paymentdetails.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

/**
 * 
 */
@Data
@Builder
public class PaymentDetailsResponseDTO {
	    private Long id;
	    private Double amount;
	    private Double totalAmount;
	    private String paymentMode;
	    private String receiptNumber;
	    private String remark;
	    private LocalDateTime paymentDate;
	    private Long guestDetailsId;
	    private Boolean isDeleted;
	    private Boolean isActive;
	    private Boolean isRefund;
		private Double refundAmount;
		private String refundType;
		private String transactionId;
		private String refundAccountNo;
		
	    
	    public PaymentDetailsResponseDTO()
	    {}	    
	    public PaymentDetailsResponseDTO(Long id, Double amount, Double totalAmount, String paymentMode,
				String receiptNumber, String remark, LocalDateTime paymentDate,Long guestDetailsId, Boolean isDeleted,Boolean isActive,
				Boolean isRefund,Double refundAmount,String refundType,String transactionId,String refundAccountNo) {
			super();
			this.id = id;
			this.amount = amount;
			this.totalAmount = totalAmount;
			this.paymentMode = paymentMode;
			this.receiptNumber = receiptNumber;
			this.remark = remark;
			this.paymentDate = paymentDate;
			this.guestDetailsId = guestDetailsId;
			this.isDeleted=isDeleted;
			this.isActive=isActive;
			this.isRefund=isRefund;
			this.refundAmount=refundAmount;
			this.refundType=refundType;
			this.transactionId=transactionId;
			this.refundAccountNo=refundAccountNo;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Double getAmount() {
			return amount;
		}
		public void setAmount(Double amount) {
			this.amount = amount;
		}
		public Double getTotalAmount() {
			return totalAmount;
		}
		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}
		public String getPaymentMode() {
			return paymentMode;
		}
		public void setPaymentMode(String paymentMode) {
			this.paymentMode = paymentMode;
		}
		public String getReceiptNumber() {
			return receiptNumber;
		}
		public void setReceiptNumber(String receiptNumber) {
			this.receiptNumber = receiptNumber;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public LocalDateTime getPaymentDate() {
			return paymentDate;
		}
		public void setPaymentDate(LocalDateTime paymentDate) {
			this.paymentDate = paymentDate;
		}
		
}

