/**
 * 
 */
package com.pms.guestdetails.dto;

/**
 * 
 */

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomActivityResponseDTO {

    private Long roomMasterId;
    private Long guestDetailsId;
    private String guestName;
    private LocalDateTime checkInDateTime;
    private LocalDateTime checkOutDateTime;
    private String guestDetailsStatus;
    private Long personalDetailsId;
    private LocalDateTime createdOn;
    
}