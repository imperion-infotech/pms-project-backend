/**
 * 
 */
package com.pms.taxmaster.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pms.common.service.SoftDeleteService;
import com.pms.exception.ResourceNotFoundException;
import com.pms.search.specification.TaxMasterSpecification;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.service.BaseHotelService;
import com.pms.taxmaster.dao.ITaxMasterDAO;
import com.pms.taxmaster.dao.TaxMastersRepository;
import com.pms.taxmaster.entity.TaxMaster;
import com.pms.taxmaster.service.ITaxMasterService;

/**
 * 
 */
@Service
public class TaxMasterServiceImpl extends BaseHotelService implements ITaxMasterService {
	
	static final Logger logger = LoggerFactory.getLogger(TaxMasterServiceImpl.class);
	
	@Autowired
	private ITaxMasterDAO dao;
	
	@Autowired
	private TaxMastersRepository taxMastersRepository;
	
	@Autowired
	private SoftDeleteService softDeleteService;
	
	

	public TaxMasterServiceImpl(ITaxMasterDAO dao, TaxMastersRepository taxMastersRepository) {
		super();
		this.dao = dao;
		this.taxMastersRepository = taxMastersRepository;
	}

	public List<TaxMaster> getTaxMasters() {
		Long hotelId = HotelContext.getHotelId();

	    if (hotelId == null) {
	        throw new ResourceNotFoundException("Hotel not selected");
	    }
	    validateHotelAccess(hotelId);
		
		if (isSuperAdmin()) 
	    	return taxMastersRepository.findByIsDeletedFalse();
	    else 
	    	return taxMastersRepository.findByIsDeletedFalseAndHotelId(HotelContext.getHotelId());
		
	}

	public TaxMaster createTaxMaster(TaxMaster taxMaster) {
		Long userId = UserContext.getUserId();

	    if (userId == null) {
	        throw new ResourceNotFoundException("User not selected");
	    }
	    assignHotel(taxMaster, taxMaster.getHotelId());
	    taxMaster.setCreatedBy(userId);
		 return taxMastersRepository.saveAndFlush(taxMaster);
	}

	public TaxMaster updateTaxMaster(Long taxMasterId, TaxMaster taxMaster) {
		validateHotelAccess(taxMaster.getHotelId());
		Long userId = UserContext.getUserId();
	    if (userId == null) {
	        throw new ResourceNotFoundException("User not selected");
	    }
	    taxMaster.setUpdatedBy(userId);
	    taxMaster.setUpdatedOn(LocalDateTime.now());
		return dao.updateTaxMaster(taxMasterId, taxMaster);
	}

	public TaxMaster getTaxMaster(int taxMasterId) {
		Long hotelId = HotelContext.getHotelId();

	    if (hotelId == null) {
	        throw new ResourceNotFoundException("Hotel not selected");
	    }
		validateHotelAccess(hotelId);
		return taxMastersRepository.findByIdAndHotelId(taxMasterId,HotelContext.getHotelId());
	}

	public boolean deleteTaxMaster(int taxMasterId) {
			TaxMaster b = getTaxMaster(taxMasterId);
			validateHotelAccess(b.getHotelId());
			TaxMaster c = softDeleteService.softDelete(taxMasterId, taxMastersRepository);
		return c == null ? false :true;
	}
	
	
	 @Override
	 public List<TaxMaster> search(String taxMasterName, String taxTypeEnum) {
		 Long hotelId = HotelContext.getHotelId(); 

	     if (hotelId == null) {
	         throw new ResourceNotFoundException("Hotel not selected");
	     }
	     validateHotelAccess(hotelId);
	        Specification<TaxMaster> spec = Specification
	        		.where(TaxMasterSpecification.hasHotelId(hotelId)) 
	                .and(TaxMasterSpecification.hasTaxMasterName(taxMasterName))
	                .and(TaxMasterSpecification.hasTaxTypeEnum(taxTypeEnum));

	        return taxMastersRepository.findAll(spec);
	    }

	 @Override
	 public TaxMaster getTaxMasterByIdAndHotelID(Integer id) {
		 return taxMastersRepository.findByIdAndHotelId(id,HotelContext.getHotelId());
	 }

	
}