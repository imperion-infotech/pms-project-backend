/**
 * 
 */
package com.pms.floor.entity;

import java.util.List;

/**
 * 
 */
public class FloorMapper {

    public static FloorResponseDTO toDTO(Floor floor) {

        FloorResponseDTO dto = new FloorResponseDTO();
        dto.setId(floor.getId());
        dto.setName(floor.getName());
        dto.setDescription(floor.getDescription());

        if (floor.getRooms() != null) {

            List<RoomResponseDTO> roomDTOs = floor.getRooms().stream().map(room -> {

                RoomResponseDTO r = new RoomResponseDTO();
                r.setId(room.getId());
                r.setRoomName(room.getRoomName());
//                r.setRoomStatus(room.getRoomStatus());

//                if (room.getRoomType() != null) {
//                   // r.setRoomTypeName(room.getRoomType().getRoomTypeName());
//                }

//                if (room.getBuilding() != null) {
//                    r.setBuildingName(room.getBuilding().getName());
//                }

                return r;

            }).toList();

//            dto.setRooms(roomDTOs);
//            dto.setNoOfRooms(roomDTOs.size());
        }

        return dto;
    }
}
