/**
 * 
 */
package com.pms.guestprofile.service;

import org.springframework.stereotype.Service;

import com.pms.document.entity.DocumentDetails;
import com.pms.document.service.IDocumentDetailsService;
import com.pms.guestdetails.GuestDetails;
import com.pms.guestdetails.service.IGuestDetailsService;
import com.pms.guestprofile.DocumentInfoDto;
import com.pms.guestprofile.GuestInfoDto;
import com.pms.guestprofile.GuestProfileResponseDto;
import com.pms.guestprofile.PersonalInfoDto;
import com.pms.guestprofile.RentInfoDto;
import com.pms.guestprofile.StayInfoDto;
import com.pms.personaldetails.PersonalDetails;
import com.pms.personaldetails.service.IPersonalDetailsService;
import com.pms.rent.RentDetails;
import com.pms.rent.services.IRentDetailsService;
import com.pms.stay.entity.StayDetails;
import com.pms.stay.service.IStayDetailsService;

import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Service
@RequiredArgsConstructor
public class GuestProfileServiceImpl implements IGuestProfileService {
	
	 	private final IGuestDetailsService guestDetailsService;
	    private final IPersonalDetailsService personalDetailsService;
	    private final IDocumentDetailsService documentDetailsService;
	    private final IStayDetailsService stayDetailsService;
	    private final IRentDetailsService rentDetailsService;

	    public GuestProfileResponseDto getGuestProfile(Long guestDetailsId) {

	        GuestDetails guest = guestDetailsService.getGuestDetail(guestDetailsId);
	        DocumentDetails document = null;
	        StayDetails stay = null;
	        RentDetails rent = null;
	        GuestProfileResponseDto response = null;

	        PersonalDetails personal =
	                personalDetailsService.getById(guest.getPersonalDetailsId().longValue());

	        if(guest.getDocumentDetailsId()!= null)
	        {
	        	document =
	                documentDetailsService.getDocumentDetail(guest.getDocumentDetailsId().longValue());
	        }

	        if(guest.getStayDetailsId()!= null)
	        {
	        	stay =
	                stayDetailsService.getStayDetail(guest.getStayDetailsId().longValue());
	        }

	        if(guest.getRentDetailsId()!= null)
	        {
	          rent =
	                rentDetailsService.findById(guest.getRentDetailsId().longValue());
	        }

	       
	         response = new GuestProfileResponseDto();
	       
	        
	        GuestInfoDto guestInfoDto= guest != null ? GuestInfoDto.fromEntity(guest): new GuestInfoDto();
	        PersonalInfoDto personalInfoDto= personal != null ? PersonalInfoDto.fromEntity(personal): new PersonalInfoDto();
	        DocumentInfoDto documentInfoDto= document != null ? DocumentInfoDto.fromEntity(document): new DocumentInfoDto();
	        StayInfoDto stayInfoDto= stay != null ? StayInfoDto.fromEntity(stay): new StayInfoDto();
	        RentInfoDto rentInfoDto= rent != null ? RentInfoDto.fromEntity(rent): new RentInfoDto();
	        
         	response.setGuestInfo(guestInfoDto); 
	        response.setPersonalInfo(personalInfoDto);
	        response.setDocumentInfo(documentInfoDto);
	        response.setStayInfo(stayInfoDto);
	        response.setRentInfo(rentInfoDto);

	        return response;
	    }

}
