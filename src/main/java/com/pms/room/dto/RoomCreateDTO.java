/**
 * 
 */
package com.pms.room.dto;

import lombok.Data;

/**
 * 
 */
@Data
public class RoomCreateDTO {

    private String roomName;

    private String roomShortName;

    private Long hotelId;

    private Long floorId;

    private Long roomTypeId;

    private Long roomStatusId;

    private String rowName;

    private Boolean smoking;

    private Boolean handicap;

    private Boolean nonRoom;
    
    private Boolean nonSmoking;
}