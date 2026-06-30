/**
 * 
 */
package com.pms.security.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pms.hotel.entity.Hotel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 */
@Getter
@Setter
@ToString(exclude = {"user", "hotel"})  // ✅ avoid recursion
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class UserHotelMapping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_hotel_mapping_seq")
	@SequenceGenerator(
	    name = "user_hotel_mapping_seq",
	    sequenceName = "user_hotel_mapping_seq",
	    allocationSize = 1
	)
	@EqualsAndHashCode.Include
	private Long id;

//	 @ManyToOne
//	 @JoinColumn(name = "user_id")
//	 @JsonBackReference
//	 private User user;

//	    @ManyToOne
//	    private Hotel hotel;
	    
	    
	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    @JsonBackReference
	    private User user;

	    @ManyToOne
	    @JoinColumn(name = "hotel_id")
	    private Hotel hotel;
	    
	    

	    private String role; // ADMIN / STAFF

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public Hotel getHotel() {
			return hotel;
		}

		public void setHotel(Hotel hotel) {
			this.hotel = hotel;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}
}
