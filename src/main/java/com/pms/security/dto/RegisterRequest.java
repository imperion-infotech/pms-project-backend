/**
 * 
 */
package com.pms.security.dto;

import lombok.Data;

/**
 * 
 */
@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String role; // Optional: e.g., ROLE_USER
    private String emailId;
    private Long hotelId;
    
    
    
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

}
