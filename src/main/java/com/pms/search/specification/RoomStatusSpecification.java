/**
 * 
 */
package com.pms.search.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pms.roomstatus.entity.RoomStatus;

/**
 * 
 */
public class RoomStatusSpecification {
	
	public static Specification<RoomStatus> hasName(String roomStatusName) {
        return (root, query, cb) -> {
            if (roomStatusName == null || roomStatusName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("roomStatusName")), "%" + roomStatusName.toLowerCase() + "%");
        };
    }
	
	public static Specification<RoomStatus> hasDescription(String roomStatusTitle) {
        return (root, query, cb) -> {
            if (roomStatusTitle == null || roomStatusTitle.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("roomStatusTitle")), "%" + roomStatusTitle.toLowerCase() + "%");
        };
    }
	

	public static Specification<RoomStatus> hasHotelId(Long hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) {
                return cb.conjunction(); // no filter
            }
            return cb.equal(root.get("hotelId"), hotelId);
        };
    }
	

}
