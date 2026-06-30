/**
 * 
 */
package com.pms.othercharge.entity;

import java.io.Serializable;

import org.hibernate.annotations.SQLRestriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@Table(name="other_charge_details")
@Entity
@Data
//@SQLRestriction("is_deleted = false")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OtherChargeDetails extends BaseEntity implements Serializable{
	
static final Logger logger = LoggerFactory.getLogger(OtherChargeDetails.class);
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="total_charges")
	private Double totalCharges;

	@Column(name="amount")
	private Double amount;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="display_on_folio")
	private Boolean displayOnFolio;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guest_details_id")
//	@JsonBackReference
	private GuestDetails guestDetails;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "payment_type_id")
	private PaymentType paymentType;
	
	private Boolean isRefund;
	
	private Double refundAmount;
	
	private String refundType;
	
	private String transactionId;
	
	private String refundAccountNo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Double getTotalCharges() {
		return totalCharges;
	}

	public void setTotalCharges(Double totalCharges) {
		this.totalCharges = totalCharges;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean isDisplayOnFolio() {
		return displayOnFolio;
	}

	public void setDisplayOnFolio(Boolean displayOnFolio) {
		this.displayOnFolio = displayOnFolio;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
