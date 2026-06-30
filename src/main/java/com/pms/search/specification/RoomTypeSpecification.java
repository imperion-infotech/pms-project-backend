/**
 * 
 */
package com.pms.search.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pms.floor.entity.Floor;
import com.pms.roomtype.entity.RoomType;

/**
 * 
 */
public class RoomTypeSpecification {
	
	
	
	public static Specification<RoomType> hasRoomTypeName(String roomTypeName) {
        return (root, query, cb) -> {
            if (roomTypeName == null || roomTypeName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("roomTypeName")), "%" + roomTypeName.toLowerCase() + "%");
        };
    }
	
	
	public static Specification<RoomType> hasShortName(String shortName) {
        return (root, query, cb) -> {
            if (shortName == null || shortName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("shortName")), "%" + shortName.toLowerCase() + "%");
        };
    }
	
	public static Specification<RoomType> hasHotelId(Long hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) {
                return cb.conjunction(); // no filter
            }
            return cb.equal(root.get("hotelId"), hotelId);
        };
    }
	

}
