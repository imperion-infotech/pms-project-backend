/**
 * 
 */
package com.pms.search.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pms.floor.entity.Floor;
import com.pms.roomtype.entity.RoomType;
import com.pms.taxmaster.entity.TaxMaster;

/**
 * 
 */
public class TaxMasterSpecification {
	
	public static Specification<TaxMaster> hasHotelId(Long hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) {
                return cb.conjunction(); // no filter
            }
            return cb.equal(root.get("hotelId"), hotelId);
        };
    }
	
	public static Specification<TaxMaster> hasTaxMasterName(String taxMasterName) {
        return (root, query, cb) -> {
            if (taxMasterName == null || taxMasterName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("taxMasterName")), "%" + taxMasterName.toLowerCase() + "%");
        };
    }

	
	public static Specification<TaxMaster> hasTaxTypeEnum(String taxTypeEnum) {
        return (root, query, cb) -> {
            if (taxTypeEnum == null || taxTypeEnum.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("taxTypeEnum")), "%" + taxTypeEnum.toLowerCase() + "%");
        };
    }
	
	
}
