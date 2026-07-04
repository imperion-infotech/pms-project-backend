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
			  LocalDateTime createdFromDate, LocalDateTime createdToDate,
		        Long hotelId) {

		  return (root, query, cb) -> {
		        List<Predicate> predicates = new ArrayList<>();

		        if (hotelId != null) {
		            predicates.add(cb.equal(root.get("hotelId"), hotelId));
		        }

		        if (createdFromDate != null) {
		            predicates.add(cb.greaterThanOrEqualTo(root.get("createdOn"), createdFromDate));
		        }
		        if (createdToDate != null) {
		            predicates.add(cb.lessThanOrEqualTo(root.get("createdOn"), createdToDate));
		        }

		        return cb.and(predicates.toArray(new Predicate[0]));
		    };
	    }
}
