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
    
    public GuestSearchResponseDTO(
            Long guestId,
            String firstName,
            String lastName,
            String documentNumber,
            String roomName,
            LocalDateTime checkInDate,
            LocalDateTime checkOutDate,
            Double totalRental,Long hotelId) {

        this.guestId = guestId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.documentNumber = documentNumber;
        this.roomName = roomName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalRental = totalRental;
        this.hotelId=hotelId;
    }
}