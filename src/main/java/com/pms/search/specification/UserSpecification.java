package com.pms.search.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pms.security.entity.User;

public class UserSpecification {
	
	public static Specification<User> isNotDeleted() {
	    return (root, query, criteriaBuilder) ->
	            criteriaBuilder.isFalse(root.get("isDeleted"));
	}
	
	public static Specification<User> hasHotelId(Long hotelId) {
	    return (root, query, cb) -> {
	        if (hotelId == null) return cb.conjunction();

	        // ✅ JOIN with mappings
	        return cb.equal(root.join("mappings").join("hotel").get("id"), hotelId);
	    };
	}
	
	
	public static Specification<User> hasUsername(String username) {
	    return (root, query, cb) -> {
	        if (username == null || username.isEmpty()) {
	            return cb.conjunction();
	        }
	        return cb.like(cb.lower(root.get("username")),
	                "%" + username.toLowerCase() + "%");
	    };
	}
	
	
	public static Specification<User> hasEnabled(Boolean enabled) {
	    return (root, query, cb) -> {
	        if (enabled == null) {
	            return cb.conjunction();
	        }
	        return cb.equal(root.get("enabled"), enabled);
	    };
	}
	
	

}
