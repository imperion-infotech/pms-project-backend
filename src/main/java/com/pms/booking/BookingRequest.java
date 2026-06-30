/**
 * 
 */
package com.pms.booking;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
public class BookingRequest {

	 	private String firstName;
	    private String lastName;
	    private LocalDateTime checkIn;
	    private LocalDateTime checkOut;
	    private Long hotelId;
	    private String status;
	    private Long roomTypeId;
	    private List<String> roomFeatures;
	    private Long guestDetailsId;
	    private Long rentDetailsId;
	    private String source;
	    
		
	    
}
