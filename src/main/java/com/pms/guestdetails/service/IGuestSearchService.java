/**
 * 
 */
package com.pms.guestdetails.service;

import java.util.List;

import com.pms.guestdetails.dto.GuestSearchRequestDTO;
import com.pms.guestdetails.dto.GuestSearchResponseDTO;

/**
 * 
 */
public interface IGuestSearchService {

	List<GuestSearchResponseDTO> searchGuests(GuestSearchRequestDTO request);

}
