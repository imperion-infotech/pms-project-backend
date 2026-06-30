/**
 * 
 */
package com.pms.guestdetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pms.baseentity.BaseEntity;
import com.pms.paymentdetails.entity.PaymentDetails;
import com.pms.room.entity.RoomMaster;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name="guest_details")
@SQLRestriction("is_deleted = false")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GuestDetails extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="guest_details_id")
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "room_master_id", insertable = false, updatable = false)
	@JsonIgnore
    private RoomMaster roomMaster;
	
	@Column(name="room_master_id")
	private Integer roomMasterId;
	
	
	// ✅ FIX: owning side
//    @ManyToOne
//    @JoinColumn(name = "personal_details_id")
//    private PersonalDetails personalDetails;
	
//	@OneToMany(mappedBy = "guestDetails", cascade = CascadeType.ALL)
////    @JsonIgnore
//    private List<PaymentDetails> paymentDetails;
    
    @Column(name="personal_details_id")
    private Integer personalDetailsId;
    
    @Column(name="document_details_id")
    private Integer documentDetailsId;
    
    @Column(name="rent_details_id")
    private Integer rentDetailsId;
    
    @Column(name="stay_details_id")
    private Integer stayDetailsId;
    
    @OneToMany(mappedBy = "guestDetails", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PaymentDetails> paymentDetails;
    

    // ✅ FIX: One guest → many documents
//    @OneToMany(mappedBy = "guest")
//    @JsonIgnore
//    private List<DocumentDetails> documentDetails;

    // ✅ FIX: One guest → one rent
//    @OneToOne(cascade = CascadeType.ALL)
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "rent_id")
//    @JsonIgnore
//    private RentDetails rentDetails;
    
    
 // ✅ FIX: owning side
//    @OneToOne
//    @JoinColumn(name = "stay_details_id")
//    @JsonIgnore
//    private StayDetails stayDetails;
    
	public List<PaymentDetails> getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(List<PaymentDetails> paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	@Column(name="check_in_date")
	private LocalDateTime checkInDate;

	@Column(name="check_out_date")
	private LocalDateTime checkOutDate;
	
	@Column(name="check_in_time")
	private LocalTime  checkInTime;
	
	@Column(name="check_out_time")
	private LocalTime checkOutTime;
	
	@Column(name="no_of_days")
	private Integer noOfDays;
	
	@Column(name="guest_details_status")
	private String guestDetailsStatus;
	
	 // ✅ NEW FIELDS (Soft Delete)
//    @Column(name = "is_deleted")
//    private boolean isDeleted = false;
//
//    private LocalDateTime deletedOn;
//
//    private Long deletedBy;
    
    @Column(name = "request_trace_id")
    private String requestTraceId;

    @Column(name = "business_trace_id")
    private String businessTraceId;
    
//    @Column(name = "guest_current_status")
//    private String guestCurrentStatus;
	
	
	
}
