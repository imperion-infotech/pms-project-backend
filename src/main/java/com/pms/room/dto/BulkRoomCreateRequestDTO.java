/**
 * 
 */
package com.pms.room.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
public class BulkRoomCreateRequestDTO {

    private Long hotelId;

    private Long floorId;

    private Long roomTypeId;

    /*
     * ROOM STRUCTURE
     */
    private Integer totalRows;

    private Integer roomsPerRow;

    /*
     * OPTIONAL PREFIX
     */
    private String prefix;
    
    private List<RoomCreateDTO> rooms;

}