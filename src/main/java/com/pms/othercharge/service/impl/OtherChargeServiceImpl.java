/**
 * 
 */
package com.pms.othercharge.service.impl;


import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pms.common.service.SoftDeleteService;
import com.pms.exception.ResourceNotFoundException;
import com.pms.othercharge.entity.OtherCharge;
import com.pms.othercharge.repository.OtherChargeRepository;
import com.pms.othercharge.service.IOtherChargeService;
import com.pms.search.specification.OtherChargeSpecification;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.service.BaseHotelService;

/**
 * 
 */
@Service
public class OtherChargeServiceImpl extends BaseHotelService implements IOtherChargeService{
	
public static final Logger logger = LoggerFactory.getLogger(OtherChargeServiceImpl.class);
	
	@Autowired
	private OtherChargeRepository otherChargeRepository;
	
	@Autowired
	private SoftDeleteService softDeleteService;
	
	public OtherChargeServiceImpl(OtherChargeRepository otherChargeRepository) {
		super();
		this.otherChargeRepository = otherChargeRepository;
	}

	@Override
	public List<OtherCharge> getOtherCharges() {
		Long hotelId = HotelContext.getHotelId();
	    if (hotelId == null) {
	        throw new ResourceNotFoundException("Hotel not selected");
	    }
	    validateHotelAccess(hotelId);
	    if (isSuperAdmin()) 
	    	return otherChargeRepository.findByIsDeletedFalse();
	    else 
		return otherChargeRepository.findByIsDeletedFalseAndHotelId(HotelContext.getHotelId());
	}

	@Override
	public OtherCharge createOtherCharge(OtherCharge otherCharge) {
		Long userId = UserContext.getUserId();

	    if (userId == null) {
	        throw new ResourceNotFoundException("User not selected");
	    }
//	    validateHotelAccess(otherCharge.getHotelId());
	    assignHotel(otherCharge, otherCharge.getHotelId());
	    otherCharge.setCreatedBy(userId);
		return otherChargeRepository.save(otherCharge);
	}

	@Override
	public OtherCharge updateOtherCharge(Long otherChargeId, OtherCharge otherCharge) {
		Long userId = UserContext.getUserId();
	    if (userId == null) {
	        throw new ResourceNotFoundException("User not selected");
	    }
	    validateHotelAccess(otherCharge.getHotelId());
				OtherCharge otherChargeFromDB = getOtherChargeById(otherChargeId);
				otherChargeFromDB.setAlwaysCharge(otherCharge.isAlwaysCharge());
				otherChargeFromDB.setCallLoggingCharge(otherCharge.isCallLoggingCharge());
				otherChargeFromDB.setCreatedOn(otherCharge.getCreatedOn());
				otherChargeFromDB.setCallPOSCharge(otherCharge.isCallPOSCharge());
				otherChargeFromDB.setCategoryName(otherCharge.getCategoryName());;
				otherChargeFromDB.setOtherChargeName(otherCharge.getOtherChargeName());
				otherChargeFromDB.setOtherChargeShortName(otherCharge.getOtherChargeShortName());
				otherChargeFromDB.setCrsCharge(otherCharge.isCrsCharge());
				otherChargeFromDB.setForeCastingRevenue(otherCharge.isForeCastingRevenue());
				otherChargeFromDB.setReoccureCharge(otherCharge.isReoccureCharge());
				otherChargeFromDB.setReoccureChargeFrequency(otherCharge.getReoccureChargeFrequency());
			    otherChargeFromDB.setUpdatedBy(userId);
			    otherChargeFromDB.setUpdatedOn(LocalDateTime.now());
				otherChargeRepository.save(otherChargeFromDB);
				OtherCharge updatedOtherCharge = getOtherChargeById(otherChargeId);
				return updatedOtherCharge;
	}

	@Override
	public OtherCharge getOtherChargeById(Long id) {
		Long hotelId = HotelContext.getHotelId();

	    if (hotelId == null) {
	        throw new ResourceNotFoundException("Hotel not selected");
	    }
	    validateHotelAccess(hotelId);
		  return otherChargeRepository.findByIdAndHotelId(id,hotelId);
	}

	@Override
	public boolean deleteOtherCharge(Long otherChargeId) {
		Long hotelId = HotelContext.getHotelId();
		 if (hotelId == null) {
	         throw new ResourceNotFoundException("Hotel not selected");
	     }
		 validateHotelAccess(hotelId);
		 OtherCharge b = softDeleteService.softDelete(otherChargeId, otherChargeRepository);
		return b == null ? false : true;
		 
	}

	@Override
	public List<OtherCharge> search(String otherChargeName, String otherChargeShortName, String categoryName) {
		 Long hotelId = HotelContext.getHotelId();   // 🔥 get from JWT
	     if (hotelId == null) {
	         throw new ResourceNotFoundException("Hotel not selected");
	     }
	     validateHotelAccess(hotelId);
		 Specification<OtherCharge> spec = Specification
				 	.where(OtherChargeSpecification.hasHotelId(hotelId)) 
	                .and(OtherChargeSpecification.hasOtherChargeName(otherChargeName))
	                .and(OtherChargeSpecification.hasOtherChargeShortName(otherChargeShortName))
	        		.and(OtherChargeSpecification.hasCategoryName(categoryName));

	        return otherChargeRepository.findAll(spec);
	    }

	}