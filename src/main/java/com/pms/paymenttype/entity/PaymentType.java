/**
 * 
 */
package com.pms.paymenttype.entity;

import java.io.Serializable;

import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pms.baseentity.BaseEntity;
import com.pms.paymentdetails.entity.PaymentDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="payment_type")
@Entity
@Data
@SQLRestriction("is_deleted = false")
public class PaymentType extends BaseEntity implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="payment_type_name")
	private String paymentTypeName;
	
	@Column(name="payment_type_short_name")
	private String paymentTypeShortName;
	
	@Column(name="category_name")
	private String categoryName;
	
	@Column(name="description")
	private String description;
	
	@Column(name="is_creditcard_processing")
	private Boolean creditCardProcessing;
	
	@ManyToOne
	@JoinColumn(name = "payment_details_id")
	@JsonIgnore
	private PaymentDetails paymentDetails;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaymentTypeName() {
		return paymentTypeName;
	}

	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}

	public String getPaymentTypeShortName() {
		return paymentTypeShortName;
	}

	public void setPaymentTypeShortName(String paymentTypeShortName) {
		this.paymentTypeShortName = paymentTypeShortName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean isCreditCardProcessing() {
		return creditCardProcessing;
	}

	public void setCreditCardProcessing(Boolean creditCardProcessing) {
		this.creditCardProcessing = creditCardProcessing;
	}

}
