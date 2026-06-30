/**
 * 
 */
package com.pms.search.specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.pms.guestdetails.GuestDetails;

import jakarta.persistence.criteria.Predicate;

/**
 * 
 */
@Component
public class GuestSpecification {

	  public Specification<GuestDetails> search(
	            LocalDateTime checkInFromDate, LocalDateTime checkInToDate,
	            LocalDateTime checkOutFromDate, LocalDateTime checkOutToDate,
	            Long hotelId) {

	        return (root, query, cb) -> {
	            List<Predicate> predicates = new ArrayList<>();
	            
	            if (hotelId != null) {
	                predicates.add(cb.equal(root.get("hotelId"), hotelId));
	            }

	            // Date filters (these fields exist directly on GuestDetails)
	            if (checkInFromDate != null) {
	                predicates.add(cb.greaterThanOrEqualTo(root.get("checkInDate"), checkInFromDate));
	            }
	            if (checkInToDate != null) {
	                predicates.add(cb.lessThanOrEqualTo(root.get("checkInDate"), checkInToDate));
	            }
	            if (checkOutFromDate != null) {
	                predicates.add(cb.greaterThanOrEqualTo(root.get("checkOutDate"), checkOutFromDate));
	            }
	            if (checkOutToDate != null) {
	                predicates.add(cb.lessThanOrEqualTo(root.get("checkOutDate"), checkOutToDate));
	            }

	            return cb.and(predicates.toArray(new Predicate[0]));
	        };
	    }
}
