/**
 * 
 */
package com.pms.guestprofile.service;

import com.pms.guestprofile.GuestProfileResponseDto;

/**
 * 
 */
public interface IGuestProfileService {
	
	 public GuestProfileResponseDto getGuestProfile(Long guestDetailsId);

}
