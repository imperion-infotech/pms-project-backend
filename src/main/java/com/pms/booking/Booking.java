/**
 * 
 */
package com.pms.booking;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pms.baseentity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Booking extends BaseEntity{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String status;
    private Long roomTypeId;
    private List<String> roomFeatures;
    private Long guestDetailsId;
    private Long rentDetailsId;
    private String source;
    
    
//    @ManyToOne
//    @JoinColumn(name = "guest_details_id")
//    private GuestDetails guestDetails;
    
    
    
}
