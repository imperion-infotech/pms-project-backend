/**
 * 
 */
package com.pms.document.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pms.baseentity.BaseEntity;
import com.pms.guestdetails.GuestDetails;
import com.pms.personaldetails.PersonalDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name="document_details")
@SQLRestriction("is_deleted = false")
public class DocumentDetails extends BaseEntity implements Serializable{
	
static final Logger logger = LoggerFactory.getLogger(DocumentDetails.class);
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="document_number")
	private String documentNumber;
	
	@Column(name="valid_till")
	private Date validTill;
	
	@Column(name="front_image_path")
	private String frontImagePath;
	
	@Column(name="back_image_path")
	private String backImagePath;
	
	@Column(name="remark")
	private String remark;
	
	
	
	 // ✅ FIX: Many documents → one personal
    @ManyToOne
    @JoinColumn(name = "personal_details_id")
    private PersonalDetails personalDetails;

    // ✅ FIX: Many documents → one guest
    @ManyToOne
    @JoinColumn(name = "guest_id")
    private GuestDetails guest;

    // ✅ FIX: remove insertable=false
    @ManyToOne
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;
    
    @Column(name = "request_trace_id")
    private String requestTraceId;

    @Column(name = "business_trace_id")
    private String businessTraceId;
	
	public String getRequestTraceId() {
		return requestTraceId;
	}

	public void setRequestTraceId(String requestTraceId) {
		this.requestTraceId = requestTraceId;
	}

	public String getBusinessTraceId() {
		return businessTraceId;
	}

	public void setBusinessTraceId(String businessTraceId) {
		this.businessTraceId = businessTraceId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public Date getValidTill() {
		return validTill;
	}

	public void setValidTill(Date validTill) {
		this.validTill = validTill;
	}

	public String getFrontImagePath() {
		return frontImagePath;
	}

	public void setFrontImagePath(String frontImagePath) {
		this.frontImagePath = frontImagePath;
	}

	public String getBackImagePath() {
		return backImagePath;
	}

	public void setBackImagePath(String backImagePath) {
		this.backImagePath = backImagePath;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public PersonalDetails getPersonalDetails() {
		return personalDetails;
	}

	public void setPersonalDetails(PersonalDetails personalDetails) {
		this.personalDetails = personalDetails;
	}


	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}
	


	

	

	
}