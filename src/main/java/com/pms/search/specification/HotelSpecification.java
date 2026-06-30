/**
 * 
 */
package com.pms.search.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pms.hotel.entity.Hotel;
import com.pms.personaldetails.PersonalDetails;

/**
 * 
 */
public class HotelSpecification {
	
	public static Specification<Hotel> isNotDeleted() {
	    return (root, query, criteriaBuilder) ->
	            criteriaBuilder.isFalse(root.get("isDeleted"));
	}
	
	public static Specification<Hotel> hasHotelId(Long hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) {
                return cb.conjunction(); // no filter
            }
            return cb.equal(root.get("hotelId"), hotelId);
        };
    }
	
	
	public static Specification<Hotel> hasHotelName(String hotelName) {
        return (root, query, cb) -> {
            if (hotelName == null || hotelName.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("hotelName")), "%" + hotelName.toLowerCase() + "%");
        };
    }
	
	public static Specification<Hotel> hasUrl(String url) {
        return (root, query, cb) -> {
            if (url == null || url.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("url")), "%" + url.toLowerCase() + "%");
        };
    }
	
	public static Specification<Hotel> hasAddress(String address) {
        return (root, query, cb) -> {
            if (address == null || address.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
        };
    }
	
	public static Specification<Hotel> hasCity(String city) {
        return (root, query, cb) -> {
            if (city == null || city.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%");
        };
    }
	
	public static Specification<Hotel> hasState(String state) {
        return (root, query, cb) -> {
            if (state == null || state.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("state")), "%" + state.toLowerCase() + "%");
        };
    }
	
	public static Specification<Hotel> hasCountry(String country) {
        return (root, query, cb) -> {
            if (country == null || country.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("country")), "%" + country.toLowerCase() + "%");
        };
    }
	
	public static Specification<Hotel> hasEmail(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }
	
	public static Specification<Hotel> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("status")), "%" + status.toLowerCase() + "%");
        };
    }
	
	
	

}
