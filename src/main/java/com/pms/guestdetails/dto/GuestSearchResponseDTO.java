/**
 * 
 */
package com.pms.guestdetails.dto;

/**
 * 
 */
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GuestSearchResponseDTO {

    private Long guestId;

    private String firstName;

    private String lastName;

    private String documentNumber;
    
    private String roomName;

    private LocalDateTime checkInDate;

    private LocalDateTime checkOutDate;

    private Double totalRental;
    
    private Long hotelId;
    
    private String paymentType;
    
    private String transactionNo;
    
    private Double amountRemain;
    
    private Double amountPaid;
    
    private String documentId;
    
    private String bookingType;
    
    private String bookingRefNo;
    
//    private String documentTypeCategory;

	public GuestSearchResponseDTO(Long guestId, String firstName, String lastName, String documentNumber,
			String roomName, LocalDateTime checkInDate, LocalDateTime checkOutDate, Double totalRental, Long hotelId,
			String paymentType, String transactionNo, Double amountRemain, Double amountPaid, String documentId,
			String bookingType, String bookingRefNo) {
		super();
		this.guestId = guestId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.documentNumber = documentNumber;
		this.roomName = roomName;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.totalRental = totalRental;
		this.hotelId = hotelId;
		this.paymentType = paymentType;
		this.transactionNo = transactionNo;
		this.amountRemain = amountRemain;
		this.amountPaid = amountPaid;
		this.documentId = documentId;
		this.bookingType = bookingType;
		this.bookingRefNo = bookingRefNo;
	}

	
    
   
}