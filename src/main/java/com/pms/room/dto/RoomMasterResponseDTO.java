/**
 * 
 */
package com.pms.room.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomMasterResponseDTO {

    private Long id;

    private String roomName;
    private String roomShortName;


    private Long hotelId;
    private String hotelName;

    private Long floorId;
    private String floorName;

    private Long roomTypeId;
    private String roomTypeName;

    private Long roomStatusId;
    private String roomStatusName;

    private Boolean isActive;
    private Boolean isDeleted;
    private Boolean smoking;
    private Boolean handicap;
    private Boolean nonRoom;
    private Boolean nonSmoking;
    
    private Boolean pet;
    
    private Long buildingId;
//    private String buildingName;
    
    
    

}