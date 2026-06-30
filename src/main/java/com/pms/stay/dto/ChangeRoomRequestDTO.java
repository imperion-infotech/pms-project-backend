/**
 * 
 */
package com.pms.stay.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeRoomRequestDTO {

    @NotNull(message = "Stay id is required")
    private Long stayId;

    @NotNull(message = "New room id is required")
    private Long newRoomId;

    private String reason;
    
    private String remarks;
}