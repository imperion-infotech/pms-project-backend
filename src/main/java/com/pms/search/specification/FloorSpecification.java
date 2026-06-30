/**
 * 
 */
package com.pms.search.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pms.floor.entity.Floor;

/**
 * 
 */

public class FloorSpecification {
	
	public static Specification<Floor> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
	
	 public static Specification<Floor> hasDescription(String description) {
	        return (root, query, cb) -> {
	            if (description == null || description.isEmpty()) {
	                return cb.conjunction();
	            }
	            return cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
	        };
	    }
	
	public static Specification<Floor> hasHotelId(Long hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) {
                return cb.conjunction(); // no filter
            }
            return cb.equal(root.get("hotelId"), hotelId);
        };
    }
	
}
