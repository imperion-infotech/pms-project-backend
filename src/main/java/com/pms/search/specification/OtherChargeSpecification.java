/**
 * 
 */
package com.pms.search.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pms.floor.entity.Floor;
import com.pms.othercharge.entity.OtherCharge;

/**
 * 
 */
public class OtherChargeSpecification {
	
	public static Specification<OtherCharge> hasHotelId(Long hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) {
                return cb.conjunction(); // no filter
            }
            return cb.equal(root.get("hotelId"), hotelId);
        };
    }
	
	public static Specification<OtherCharge> hasOtherChargeName(String otherChargeName) {
        return (root, query, cb) -> {
            if (otherChargeName == null || otherChargeName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("otherChargeName")), "%" + otherChargeName.toLowerCase() + "%");
        };
    }
	
	
	public static Specification<OtherCharge> hasOtherChargeShortName(String otherChargeShortName) {
        return (root, query, cb) -> {
            if (otherChargeShortName == null || otherChargeShortName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("otherChargeShortName")), "%" + otherChargeShortName.toLowerCase() + "%");
        };
    }
	
	public static Specification<OtherCharge> hasCategoryName(String categoryName) {
        return (root, query, cb) -> {
            if (categoryName == null || categoryName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("categoryName")), "%" + categoryName.toLowerCase() + "%");
        };
    }
	
	
	
	
}
