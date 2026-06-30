/**
 * 
 */
package com.pms.search.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pms.floor.entity.Floor;
import com.pms.room.entity.RoomMaster;

/**
 * 
 */
public class RoomMasterSpecification {
	
	public static Specification<RoomMaster> hasHotelId(Long hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) {
                return cb.conjunction(); // no filter
            }
            return cb.equal(root.get("hotelId"), hotelId);
        };
    }
	
	
	public static Specification<RoomMaster> hasRoomName(String roomName) {
        return (root, query, cb) -> {
            if (roomName == null || roomName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("roomName")), "%" + roomName.toLowerCase() + "%");
        };
    }
	
	public static Specification<RoomMaster> hasRoomShortName(String roomShortName) {
        return (root, query, cb) -> {
            if (roomShortName == null || roomShortName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("roomShortName")), "%" + roomShortName.toLowerCase() + "%");
        };
    }
	
	
	public static Specification<RoomMaster> hasFloorName(String floorName) {
        return (root, query, cb) -> {
            if (floorName == null || floorName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("floorName")), "%" + floorName.toLowerCase() + "%");
        };
    }
	
	
	
	
	

}
