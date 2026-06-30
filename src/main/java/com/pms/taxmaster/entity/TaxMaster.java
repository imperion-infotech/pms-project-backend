/**
 * 
 */
package com.pms.taxmaster.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pms.baseentity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="tax_master")
@SQLRestriction("is_deleted = false")
public class TaxMaster extends BaseEntity implements Serializable{
	
static final Logger logger = LoggerFactory.getLogger(TaxMaster.class);
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="tax_master_id")
	private Long id;
	
	@Column(name="tax_master_name")
	private String taxMasterName;
	
	@NotNull(message = "Tax type is required")
	@Enumerated(EnumType.STRING) // Store enum name as text in DB
	private TaxTypeEnum taxTypeEnum;
	
	@Column(name="per_day_tax")
	private Boolean perDayTax;
	
	@Column(name="per_stay_tax")
	private Boolean perStayTax;
	
	@NotNull(message = "Amount is required")
	@Column(name="amount")
	private Double amount;
	
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaxMasterName() {
		return taxMasterName;
	}

	public void setTaxMasterName(String taxMasterName) {
		this.taxMasterName = taxMasterName;
	}

	public TaxTypeEnum getTaxTypeEnum() {
		return taxTypeEnum;
	}

	public void setTaxTypeEnum(TaxTypeEnum taxTypeEnum) {
		this.taxTypeEnum = taxTypeEnum;
	}

	public Boolean getPerDayTax() {
		return perDayTax;
	}

	public void setPerDayTax(Boolean perDayTax) {
		this.perDayTax = perDayTax;
	}

	public Boolean getPerStayTax() {
		return perStayTax;
	}

	public void setPerStayTax(Boolean perStayTax) {
		this.perStayTax = perStayTax;
	}
	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TexMaster [id=");
		builder.append(id);
		builder.append(", taxMasterName=");
		builder.append(taxMasterName);
		builder.append(", taxTypeEnum=");
		builder.append(taxTypeEnum);
		builder.append(", perDayTax=");
		builder.append(perDayTax);
		builder.append(", perStayTax=");
		builder.append(perStayTax);
		builder.append("]");
		return builder.toString();
	}
	
}
