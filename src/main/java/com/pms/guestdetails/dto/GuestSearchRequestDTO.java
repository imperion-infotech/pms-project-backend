/**
 * 
 */
package com.pms.guestdetails.dto;

/**
 * 
 */
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GuestSearchRequestDTO {

	private String guestName;

    private String documentNumber;

    private String roomName;

    private LocalDateTime checkInFromDate;

    private LocalDateTime checkInToDate;

    private LocalDateTime checkOutFromDate;

    private LocalDateTime checkOutToDate;

    private Double totalRental;
    
    private Long hotelId;
    
    private String cardNumber;
}