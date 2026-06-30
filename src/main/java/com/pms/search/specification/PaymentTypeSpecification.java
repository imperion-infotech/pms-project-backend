/**
 * 
 */
package com.pms.search.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pms.floor.entity.Floor;
import com.pms.paymenttype.entity.PaymentType;

/**
 * 
 */
public class PaymentTypeSpecification {
	
	public static Specification<PaymentType> hasHotelId(Long hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) {
                return cb.conjunction(); // no filter
            }
            return cb.equal(root.get("hotelId"), hotelId);
        };
    }
	
	public static Specification<PaymentType> hasPaymentTypeName(String paymentTypeName) {
        return (root, query, cb) -> {
            if (paymentTypeName == null || paymentTypeName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("paymentTypeName")), "%" + paymentTypeName.toLowerCase() + "%");
        };
    }
	
	public static Specification<PaymentType> hasPaymentTypeShortName(String paymentTypeShortName) {
        return (root, query, cb) -> {
            if (paymentTypeShortName == null || paymentTypeShortName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("paymentTypeShortName")), "%" + paymentTypeShortName.toLowerCase() + "%");
        };
    }
	
	public static Specification<PaymentType> hasCategoryName(String categoryName) {
        return (root, query, cb) -> {
            if (categoryName == null || categoryName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("categoryName")), "%" + categoryName.toLowerCase() + "%");
        };
    }
	
	public static Specification<PaymentType> hasDescription(String description) {
        return (root, query, cb) -> {
            if (description == null || description.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
        };
    }
	
}
