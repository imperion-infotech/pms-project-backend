/**
 * 
 */
package com.pms.rent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pms.baseentity.BaseEntity;
import com.pms.taxmaster.entity.TaxMaster;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name="rent_details")
@SQLRestriction("is_deleted = false")
public class RentDetails extends BaseEntity implements Serializable {
	
static final Logger logger = LoggerFactory.getLogger(RentDetails.class);
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="rent")
	private Double rent;
	
	@Column(name="basic")
	private Double basic;
	
	@Column(name="tax_id")
	private Long taxId;
	
	@OneToOne
    @JoinColumn(name = "tax_id", insertable = false, updatable = false)
    private TaxMaster taxMaster;
	
	@Column(name="total_rental")
	private Double totalRental;
	
	@Column(name="other_charges")
	private Double otherCharges;
	
	@Column(name="discount")
	private Double discount;
	
	@Column(name="total_charges")
	private Double totalCharges;
	
	@Column(name="payments")
	private Double payments;
	
	@Column(name="cc_authorized")
	private Double ccAuthorized;
	
	@Column(name="deposite")
	private Double deposite;
	
	@Column(name="balance")
	private Double balance;
	
//	 @OneToOne(mappedBy = "rentDetails")
//	 private GuestDetails guest;
	
	 // ✅ NEW FIELDS (Soft Delete)
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    private LocalDateTime deletedOn;

    private Long deletedBy;
    
    @Column(name = "request_trace_id")
    private String requestTraceId;

    @Column(name = "business_trace_id")
    private String businessTraceId;
    
    @Column(name = "personal_details_id")
    private Long personalDetailsId;
    
    @Column(name = "carry_forward_amount")
    private Long carryForwardAmount;
    
	
	
	
}
