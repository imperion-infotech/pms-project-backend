/**
 * 
 */
package com.pms.search.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pms.document.entity.DocumentType;
import com.pms.floor.entity.Floor;

/**
 * 
 */
public class DocumentTypeSpecification {
	
	public static Specification<DocumentType> hasHotelId(Long hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) {
                return cb.conjunction(); // no filter
            }
            return cb.equal(root.get("hotelId"), hotelId);
        };
    }
	
	public static Specification<DocumentType> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
	
	public static Specification<DocumentType> hasShortName(String shortName) {
        return (root, query, cb) -> {
            if (shortName == null || shortName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("documentTypeShortName")), "%" + shortName.toLowerCase() + "%");
        };
    }
	
	public static Specification<DocumentType> hasDocumentTypeName(String documentTypeName) {
        return (root, query, cb) -> {
            if (documentTypeName == null || documentTypeName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("documentTypeName")), "%" + documentTypeName.toLowerCase() + "%");
        };
    }
	
	public static Specification<DocumentType> hasDocumentTypeDescription(String documentTypeDescription) {
        return (root, query, cb) -> {
            if (documentTypeDescription == null || documentTypeDescription.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("documentTypeDescription")), "%" + documentTypeDescription.toLowerCase() + "%");
        };
    }
	
}
