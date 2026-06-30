/**
 * 
 */
package com.pms.othercharge.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
import com.pms.othercharge.entity.OtherChargeDetails;
import com.pms.othercharge.entity.OtherChargeDetailsResponseDTO;
import com.pms.othercharge.repository.OtherChargeDetailsRepository;
import com.pms.othercharge.service.IOtherChargeDetailsService;
import com.pms.paymentdetails.entity.PaymentDetails;
import com.pms.paymentdetails.entity.PaymentDetailsResponseDTO;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.util.SecurityUtils;

/**
 * 
 */
@Service
public class OtherChargeDetailsServiceImpl implements IOtherChargeDetailsService {
	
	@Autowired
	private OtherChargeDetailsRepository otherChargeDetailsRepository;
	
	@Autowired
	private SoftDeleteService softDeleteService;
	
	@Autowired
	private AuditLogRepository auditRepo;
	
	public OtherChargeDetailsServiceImpl(OtherChargeDetailsRepository otherChargeDetailsRepository) {
		super();
		this.otherChargeDetailsRepository = otherChargeDetailsRepository;
	}

	@Override
	public List<OtherChargeDetails> getAllOtherChargeDetails() {
		Long hotelId = HotelContext.getHotelId();

	    if (hotelId == null) {
	        throw new ResourceNotFoundException("Hotel not selected");
	    }
		return otherChargeDetailsRepository.findByHotelId(HotelContext.getHotelId());
	}

	@Auditable(action = "CREATE", entity = "OTHERCHARGEDETAILS")
	@Override
	public OtherChargeDetails createOtherChargeDetails(OtherChargeDetails otherChargeDetails) {
		
		Long userId = UserContext.getUserId();

	    if (userId == null) {
	        throw new ResourceNotFoundException("User not selected");
	    }
	    otherChargeDetails.setCreatedBy(userId);
		return otherChargeDetailsRepository.saveAndFlush(otherChargeDetails);
	}

	@Auditable(action = "UPDATE", entity = "OTHERCHARGEDETAILS")
	@Override
	public OtherChargeDetails updateOtherChargeDetails(Long otherChargeDetailsId,
			OtherChargeDetails otherChargeDetails) { 
		OtherChargeDetails otherChargeDetailsFromDB = getOtherChargeDetailsById(otherChargeDetailsId);
	
		otherChargeDetailsFromDB.setDisplayOnFolio(otherChargeDetails.isDisplayOnFolio());;
		otherChargeDetailsFromDB.setRemark(otherChargeDetails.getRemark());
		otherChargeDetailsFromDB.setTotalCharges(otherChargeDetails.getTotalCharges());
		otherChargeDetailsFromDB.setAmount(otherChargeDetails.getAmount());
		Long userId = UserContext.getUserId();
	    if (userId == null) {
	        throw new ResourceNotFoundException("User not selected");
	    }
	    otherChargeDetailsFromDB.setUpdatedBy(userId);
	    otherChargeDetailsFromDB.setUpdatedOn(LocalDateTime.now());
		otherChargeDetailsFromDB.setIsRefund(otherChargeDetails.getIsRefund());
		otherChargeDetailsFromDB.setRefundAmount(otherChargeDetails.getRefundAmount());
		otherChargeDetailsFromDB.setRefundType(otherChargeDetails.getRefundType());
		otherChargeDetailsFromDB.setTransactionId(otherChargeDetails.getTransactionId());
		otherChargeDetailsFromDB.setRefundAccountNo(otherChargeDetails.getRefundAccountNo());
		otherChargeDetailsFromDB.setGuestDetails(otherChargeDetails.getGuestDetails());
		
		otherChargeDetailsRepository.saveAndFlush(otherChargeDetailsFromDB);
		OtherChargeDetails updateOtherChargeDetails = getOtherChargeDetailsById(otherChargeDetailsId);
		return updateOtherChargeDetails;
	}

	@Override
	public OtherChargeDetails getOtherChargeDetailsById(Long id) {
		
		return otherChargeDetailsRepository.findByIdAndHotelId(id,HotelContext.getHotelId());
	}

	@Auditable(action = "DELETE", entity = "OTHERCHARGEDETAILS")
	@Override
	public boolean deleteOtherChargeDetails(Long otherChargeDetailsId) {
		
		OtherChargeDetails b= softDeleteService.softDelete(otherChargeDetailsId, otherChargeDetailsRepository);
		
		// ✅ Audit log
				AuditLog log = new AuditLog();
				log.setAction("DELETE");
				log.setEntityName("OTHER CHARGE DETAILS");
				log.setEntityId(b.getId().toString());
				log.setUsername(SecurityUtils.getCurrentUsername());
				log.setTimestamp(LocalDateTime.now());
				log.setOldValue(AuditUtil.toJson(b));
				 log.setBusinessTraceId(BusinessTraceContext.get());
		         log.setRequestTraceId(RequestTraceContext.get());
				auditRepo.save(log);
		
		return b == null ? false : true;
	}
	@Override
	public List<OtherChargeDetailsResponseDTO> getOtherChargeDetailsByGuestId(Long guestId) {

		Long hotelId = HotelContext.getHotelId();

		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}

//		List<OtherChargeDetails> otherChargeDetails = otherChargeDetailsRepository.findCharges(guestId, hotelId);
		
		List<OtherChargeDetails> otherChargeDetails = otherChargeDetailsRepository.findByGuestDetailsIdAndHotelId(guestId, hotelId);

		return otherChargeDetails.stream().map(p -> {
			OtherChargeDetailsResponseDTO dto = new OtherChargeDetailsResponseDTO();

			dto.setId(p.getId());
			dto.setAmount(p.getAmount());
			dto.setTotalCharges(p.getTotalCharges());
			dto.setRemark(p.getRemark());
			dto.setDisplayOnFolio(p.getDisplayOnFolio());
			dto.setGuestDetailsId(p.getGuestDetails().getId());
			dto.setIsRefund(p.getIsRefund());
			dto.setRefundAmount(p.getRefundAmount());
			dto.setRefundType(p.getRefundType());
			dto.setTransactionId(p.getTransactionId());
			dto.setRefundAccountNo(p.getRefundAccountNo());
			dto.setIsActive(p.getIsActive());
			dto.setIsDeleted(p.getIsDeleted());

			return dto;
		}).collect(Collectors.toList());
	}


}
