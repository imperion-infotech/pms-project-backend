/**
 * 
 */
package com.pms.search.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pms.building.entity.Building;
import com.pms.floor.entity.Floor;
import com.pms.personaldetails.PersonalDetails;

/**
 * 
 */
public class BuildingSpecification {
	
	public static Specification<Building> isNotDeleted() {
	    return (root, query, criteriaBuilder) ->
	            criteriaBuilder.isFalse(root.get("isDeleted"));
	}
	

	public static Specification<Building> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
	
	 public static Specification<Building> hasDescription(String description) {
	        return (root, query, cb) -> {
	            if (description == null || description.isEmpty()) {
	                return cb.conjunction();
	            }
	            return cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
	        };
	    }
	
	public static Specification<Building> hasHotelId(Long hotelId) {
     return (root, query, cb) -> {
         if (hotelId == null) {
             return cb.conjunction(); // no filter
         }
         return cb.equal(root.get("hotelId"), hotelId);
     };
 }

}
