/**
 * 
 */
package com.pms.search.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pms.building.entity.Building;
import com.pms.document.entity.DocumentDetails;
import com.pms.floor.entity.Floor;
import com.pms.personaldetails.PersonalDetails;

/**
 * 
 */
public class DocumentDetailsSpecification {
	
	public static Specification<DocumentDetails> hasHotelId(Long hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) {
                return cb.conjunction(); // no filter
            }
            return cb.equal(root.get("hotelId"), hotelId);
        };
    }
	
	public static Specification<DocumentDetails> isNotDeleted() {
	    return (root, query, criteriaBuilder) ->
	            criteriaBuilder.isFalse(root.get("isDeleted"));
	}
	
	public static Specification<DocumentDetails> hasDocumentTypeEnum(String documentTypeEnum) {
        return (root, query, cb) -> {
            if (documentTypeEnum == null || documentTypeEnum.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("documentTypeEnum")), "%" + documentTypeEnum.toLowerCase() + "%");
        };
    }

	public static Specification<DocumentDetails> hasDocumentNumber(String documentNumber) {
        return (root, query, cb) -> {
            if (documentNumber == null || documentNumber.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("documentNumber")), "%" + documentNumber.toLowerCase() + "%");
        };
    }
	
	

}
