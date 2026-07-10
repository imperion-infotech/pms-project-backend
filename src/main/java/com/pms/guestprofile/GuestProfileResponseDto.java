package com.pms.guestprofile;


import lombok.Data;

/**
 * 
 */
@Data
public class GuestProfileResponseDto {
	
		private GuestInfoDto guestInfo;
	    private PersonalInfoDto personalInfo;
	    private DocumentInfoDto documentInfo;
	    private StayInfoDto stayInfo;
	    private RentInfoDto rentInfo;
	    
}
