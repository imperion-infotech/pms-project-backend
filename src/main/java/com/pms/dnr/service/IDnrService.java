/**
 * 
 */
package com.pms.dnr.service;

import java.util.List;

import com.pms.dnr.dto.CreateDnrRequestDTO;
import com.pms.dnr.dto.DnrValidationResponseDTO;
import com.pms.dnr.dto.UpdateDnrGuestRequestDTO;
import com.pms.dnr.entity.DnrGuest;

/**
 * 
 */
public interface IDnrService {
	
	 DnrGuest create(CreateDnrRequestDTO dto);
	 DnrValidationResponseDTO validateGuest(Long personalDetailsId);
	 DnrGuest updateGuestDnr(Long dnrId,UpdateDnrGuestRequestDTO dto);
	 List<DnrGuest> listAllDnrGuest();
	 

}
