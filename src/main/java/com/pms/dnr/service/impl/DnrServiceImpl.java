/**
 * 
 */
package com.pms.dnr.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.auditlog.context.BusinessTraceContext;
import com.pms.auditlog.context.RequestTraceContext;
import com.pms.dnr.dto.CreateDnrRequestDTO;
import com.pms.dnr.dto.DnrValidationResponseDTO;
import com.pms.dnr.dto.UpdateDnrGuestRequestDTO;
import com.pms.dnr.entity.DnrGuest;
import com.pms.dnr.repository.DnrGuestRepository;
import com.pms.dnr.service.IDnrService;
import com.pms.exception.ResourceNotFoundException;
import com.pms.security.configuration.HotelContext;
import com.pms.security.service.AuthService;
import com.pms.security.service.BaseHotelService;

/**
 * 
 */
@Service
public class DnrServiceImpl extends BaseHotelService implements IDnrService{

	@Autowired
	private DnrGuestRepository repository;
	
	@Autowired
	private AuthService authService;

	public DnrGuest create(CreateDnrRequestDTO dto) {
		
		DnrGuest entity = new DnrGuest();
		
		assignHotel(entity, HotelContext.getHotelId());

		entity.setPersonalDetailsId(dto.getPersonalDetailsId());

		entity.setReason(dto.getReason());

		entity.setRemarks(dto.getRemarks());

		entity.setDnrStatus(true);
		
		entity.setStartDate(LocalDateTime.now());

		entity.setCreatedOn(LocalDateTime.now());
		
		entity.setCreatedBy(authService.getCurrentUser().getId());

		entity.setHotelId(HotelContext.getHotelId());
		
		entity.setBusinessTraceId(BusinessTraceContext.get());
		
		entity.setRequestTraceId(RequestTraceContext.get());

		return repository.save(entity);
	}

	public DnrValidationResponseDTO validateGuest(Long personalDetailsId) {
		
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);

		DnrValidationResponseDTO response = new DnrValidationResponseDTO();

		DnrGuest dnrGuest = repository.findByPersonalDetailsIdAndDnrStatusTrueAndIsDeletedFalse(personalDetailsId)
				.orElse(null);

		if (dnrGuest != null) {

			response.setDnr(true);

			response.setMessage("Guest is marked as DNR (Do Not Rent)");

			response.setReason(dnrGuest.getReason());

			return response;
		}

		response.setDnr(false);

		response.setMessage("Guest is allowed for check-in");
		if(dnrGuest != null && dnrGuest.getReason()!=null)
		{
			response.setReason(dnrGuest.getReason());
		} else {
			response.setReason("");
		}

		return response;
	}
	
	public DnrGuest updateGuestDnr(Long dnrId,UpdateDnrGuestRequestDTO dto) {
		
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);

        DnrGuest entity = repository.findById(dnrId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "DNR Guest not found"));

        if (dto.getReason() != null) {
            entity.setReason(dto.getReason());
        }

        if (entity.getReason() == null) {
            entity.setReason(dto.getReason());
        }

        if (dto.getDnrStatus() != null ) {
            entity.setDnrStatus(dto.getDnrStatus());
        }

        entity.setEndDate(LocalDateTime.now());
        entity.setUpdatedBy(authService.getCurrentUser().getId());
        entity.setUpdatedOn(LocalDateTime.now());

        return repository.save(entity);
    }
	
	@Override
	public List<DnrGuest> listAllDnrGuest() {

	    Long hotelId = HotelContext.getHotelId();

	    if (hotelId == null) {
	        throw new ResourceNotFoundException("Hotel not selected");
	    }

	    validateHotelAccess(hotelId);

	    return repository.findByHotelIdAndIsDeletedFalseOrderByCreatedOnDesc(hotelId);
	}

}
