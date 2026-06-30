/**
 * 
 */
package com.pms.floor.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
public class BulkFloorCreateRequestDTO {

    private Long hotelId;

    private Long buildingId;

    private Integer startFloor;

    private Integer totalFloors;
    
    private Integer noOfRooms; 
}