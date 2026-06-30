/**
 * 
 */
package com.pms.room.dto;

import java.util.List;

import com.pms.floor.entity.RoomResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BulkRoomCreateResponseDTO {

    private Long hotelId;

    private String hotelName;

    private Long floorId;

    private String floorName;

    private Long roomTypeId;

    private String roomTypeName;

    private Integer totalRoomsCreated;

    private List<RoomResponseDTO> rooms;
    
}