/**
 * 
 */
package com.pms.othercharge.entity;

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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name="other_charge")
@Entity
@Data
@SQLRestriction("is_deleted = false")
public class OtherCharge extends BaseEntity  implements Serializable{
	
static final Logger logger = LoggerFactory.getLogger(OtherCharge.class);
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="short_name")
	private String otherChargeShortName;
	
	@Column(name="charge_name")
	private String otherChargeName;
	
	@Column(name="category_name")
	private String categoryName;
	
	@Column(name="is_Taxable")
	private Boolean taxable;
	
	@Column(name="is_AlwaysCharge")
	private Boolean alwaysCharge;

	@Column(name="is_ReoccureCharge")
	private Boolean reoccureCharge;

	@Column(name="reoccureCharge_frequency")
	private Integer reoccureChargeFrequency;
	
	@Column(name="is_CRSCharge")
	private Boolean crsCharge;
	
	@Column(name="is_Call_logging_charge")
	private Boolean callLoggingCharge;
	
	@Column(name="is_pos_charge")
	private Boolean callPOSCharge;
	
	@Column(name="is_forecasting_revenue")
	private Boolean foreCastingRevenue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOtherChargeShortName() {
		return otherChargeShortName;
	}

	public void setOtherChargeShortName(String otherChargeShortName) {
		this.otherChargeShortName = otherChargeShortName;
	}

	public String getOtherChargeName() {
		return otherChargeName;
	}

	public void setOtherChargeName(String otherChargeName) {
		this.otherChargeName = otherChargeName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Boolean isTaxable() {
		return taxable;
	}

	public void setTaxable(Boolean taxable) {
		this.taxable = taxable;
	}

	public Boolean isAlwaysCharge() {
		return alwaysCharge;
	}

	public void setAlwaysCharge(Boolean alwaysCharge) {
		this.alwaysCharge = alwaysCharge;
	}

	public Boolean isReoccureCharge() {
		return reoccureCharge;
	}

	public void setReoccureCharge(Boolean reoccureCharge) {
		this.reoccureCharge = reoccureCharge;
	}

	public Integer getReoccureChargeFrequency() {
		return reoccureChargeFrequency;
	}

	public void setReoccureChargeFrequency(Integer reoccureChargeFrequency) {
		this.reoccureChargeFrequency = reoccureChargeFrequency;
	}

	public Boolean isCrsCharge() {
		return crsCharge;
	}

	public void setCrsCharge(Boolean crsCharge) {
		this.crsCharge = crsCharge;
	}

	public Boolean isCallLoggingCharge() {
		return callLoggingCharge;
	}

	public void setCallLoggingCharge(Boolean callLoggingCharge) {
		this.callLoggingCharge = callLoggingCharge;
	}

	public Boolean isCallPOSCharge() {
		return callPOSCharge;
	}

	public void setCallPOSCharge(Boolean callPOSCharge) {
		this.callPOSCharge = callPOSCharge;
	}

	public Boolean isForeCastingRevenue() {
		return foreCastingRevenue;
	}

	public void setForeCastingRevenue(Boolean foreCastingRevenue) {
		this.foreCastingRevenue = foreCastingRevenue;
	}

	
}
