/**
 * 
 */
package com.pms.paymentdetails.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pms.baseentity.BaseEntity;
import com.pms.guestdetails.GuestDetails;
import com.pms.paymenttype.entity.PaymentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="payment_details")
@Entity
@Data
//@SQLRestriction("is_deleted = false")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PaymentDetails extends BaseEntity  implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="amount")
	private Double amount;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "payment_type_id")
	private PaymentType paymentType;
	
	private Double totalAmount;
	
	@Column(name="card_no")
	private String cardNo;
	
	@Column(name="valid_till")
	private LocalDateTime validTill;
	
	@Column(name="authorization_no")
	private String authorizationNo;
	
	@Column(name="remark")
	private String remark;

	@Column(name="receipt_no")
	private String receiptNo;
	
	@Column(name="currency_symbol")
	private String currencySymbol;
	
	@Column(name="payment_date")
	private LocalDateTime paymentDate;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guest_details_id")
	@JsonBackReference
	private GuestDetails guestDetails;
	
	
	private Boolean isRefund;
	
	private Double refundAmount;
	
	private String refundType;
	
	private String transactionId;
	
	private String refundAccountNo;
	
	
	
//	private Integer guestDetailsId;
	
	public GuestDetails getGuestDetails() {
		return guestDetails;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setGuestDetails(GuestDetails guestDetails) {
		this.guestDetails = guestDetails;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
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

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	
	public String getAuthorizationNo() {
		return authorizationNo;
	}

	public void setAuthorizationNo(String authorizationNo) {
		this.authorizationNo = authorizationNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	
}

