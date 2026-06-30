/**
 * 
 */
package com.pms.rent.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.auditlog.annotation.Auditable;
import com.pms.auditlog.context.BusinessTraceContext;
import com.pms.auditlog.context.RequestTraceContext;
import com.pms.auditlog.entity.AuditLog;
import com.pms.auditlog.repository.AuditLogRepository;
import com.pms.auditlog.util.AuditUtil;
import com.pms.common.service.SoftDeleteService;
import com.pms.exception.ResourceNotFoundException;
import com.pms.personaldetails.PersonalDetails;
import com.pms.personaldetails.PersonalDetailsRepository;
import com.pms.rent.RentDetails;
import com.pms.rent.dao.IRentDetailsDAO;
import com.pms.rent.dao.impl.RentDetailsRepository;
import com.pms.rent.services.IRentDetailsService;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.service.BaseHotelService;
import com.pms.security.util.SecurityUtils;

import jakarta.transaction.Transactional;

/**
 * 
 */
@Service
public class RentDetailsServiceImpl extends BaseHotelService implements IRentDetailsService {
	
	@Autowired
	private IRentDetailsDAO dao;
	
	@Autowired
	RentDetailsRepository rentDetailsRepository;
	
	@Autowired 
	private SoftDeleteService softDeleteService;
	
	 private final AuditLogRepository auditRepo;
	 
	 @Autowired
	 private PersonalDetailsRepository personalDetailsRepository;
	 
	public RentDetailsServiceImpl(IRentDetailsDAO dao, RentDetailsRepository rentDetailsRepository,
			AuditLogRepository auditRepo) {
		super();
		this.dao = dao;
		this.rentDetailsRepository = rentDetailsRepository;
		this.auditRepo = auditRepo;
	}

	@Override
	public List<RentDetails> getRentDetails() {
		Long hotelId = HotelContext.getHotelId();
 		 if (hotelId == null) {
 	         throw new ResourceNotFoundException("Hotel not selected");
 	     }
		validateHotelAccess(hotelId);
	    if (isSuperAdmin()) 
	    	return rentDetailsRepository.findByIsDeletedFalse();
	    else 
	    	return rentDetailsRepository.findByHotelIdAndIsDeletedFalse(hotelId);
	}
	
	@Auditable(action = "CREATE", entity = "RENTDETAILS")
	public RentDetails createRentDetail(RentDetails rentDetail) {
		Long userId = UserContext.getUserId();

	    if (userId == null) {
	        throw new ResourceNotFoundException("User not selected");
	    }
	    
	    PersonalDetails personal = personalDetailsRepository
	            .findById(rentDetail.getPersonalDetailsId())
	            .orElseThrow(() -> new RuntimeException("Personal details not found"));
	        String businessTraceId = personal.getBusinessTraceId();
	        BusinessTraceContext.set(businessTraceId);
	        rentDetail.setBusinessTraceId(businessTraceId);

	    
	    assignHotel(rentDetail, rentDetail.getHotelId());
	    rentDetail.setCreatedBy(userId);
		return rentDetailsRepository.save(rentDetail);
	}
	
	@Auditable(action = "UPDATE", entity = "RENTDETAILS")
	@Override
	public RentDetails updateRentDetail(Long rentDetailsId, RentDetails rentDetail) {
		Long userId = UserContext.getUserId();
	    if (userId == null) {
	        throw new ResourceNotFoundException("User not selected");
	    }
	    Long hotelId = HotelContext.getHotelId();
		 if (hotelId == null) {
	         throw new ResourceNotFoundException("Hotel not selected");
	     }
		 BusinessTraceContext.set(rentDetail.getBusinessTraceId());
		 
	    validateHotelAccess(hotelId);
	    rentDetail.setUpdatedBy(userId);
	    rentDetail.setUpdatedOn(LocalDateTime.now());
		
		rentDetailsRepository.save(rentDetail);
		 return getRentDetail(rentDetailsId);
		
	}
	
	public RentDetails getRentDetail(Long rentDetailsId) {
		Long hotelId = HotelContext.getHotelId();
		 if (hotelId == null) {
	         throw new ResourceNotFoundException("Hotel not selected");
	     }
		 validateHotelAccess(hotelId);
		  return rentDetailsRepository.findByIdAndHotelIdAndIsDeletedFalse(Long.valueOf(rentDetailsId),hotelId);
	}

	@Override
	public RentDetails findById(Long id) {
		return dao.findById(id);
	}
	
	@Auditable(action = "DELETE", entity = "STAYDETAILS")
    @Transactional
    public boolean deleteSoftRentDetails(Long id) {

		Long hotelId = HotelContext.getHotelId();
		Long userId = UserContext.getUserId();
		if(userId == null) {
			 throw new ResourceNotFoundException("User not selected");
		}
		 
		 if (hotelId == null) {
	         throw new ResourceNotFoundException("Hotel not selected");
	     }
		 validateHotelAccess(hotelId);
		RentDetails entity = rentDetailsRepository.findByIdAndHotelIdAndIsDeletedFalse(id,hotelId);
		BusinessTraceContext.set(entity.getBusinessTraceId());
//        // ✅ Soft delete
//        entity.setDeleted(true);
//        entity.setDeletedOn(LocalDateTime.now());
//        entity.setDeletedBy(userId);
        
		RentDetails b= softDeleteService.softDelete(id, rentDetailsRepository);

//        rentDetailsRepository.save(entity);
        
        // ✅ Audit log
        AuditLog log = new AuditLog();
        log.setAction("DELETE");
        log.setEntityName("RENT DETAILS");
        log.setEntityId(entity.getId()+"");
        log.setOldValue(AuditUtil.toJson(entity));
        log.setUsername(SecurityUtils.getCurrentUsername());
        log.setTimestamp(LocalDateTime.now());
        log.setBusinessTraceId(BusinessTraceContext.get());
        log.setRequestTraceId(RequestTraceContext.get());

        auditRepo.save(log);
        return b == null?false:true;
}
	 
	public RentDetails getRentDetailByPersonalDetailsId(Long personalDetailsId) {
		
		  return rentDetailsRepository.findByPersonalDetailsId(personalDetailsId)
	                .orElseThrow(() -> new RuntimeException(
	                        "RentDetails not found for personalDetailsId : " + personalDetailsId));
	}
	
}
