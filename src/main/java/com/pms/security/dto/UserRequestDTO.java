/**
 * 
 */
package com.pms.security.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pms.othercharge.entity.OtherChargeDetailsResponseDTO;

import lombok.Builder;
import lombok.Data;

/**
 * 
 */
@Data
@Builder
public class UserRequestDTO {
    private String username;
    private String email;
    private String password;
    private Set<Long> roleIds;
    private Set<Long> hotelIds;
    private Boolean enabled;
    private String userPermissionArray;
    
    
    		
		public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public Set<Long> getRoleIds() {
			return roleIds;
		}
		public void setRoleIds(Set<Long> roleIds) {
			this.roleIds = roleIds;
		}
		public Set<Long> getHotelIds() {
			return hotelIds;
		}
		public void setHotelIds(Set<Long> hotelIds) {
			this.hotelIds = hotelIds;
		}
	    
	    
	    
}
