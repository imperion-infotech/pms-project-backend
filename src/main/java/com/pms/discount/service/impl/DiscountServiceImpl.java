/**
 * 
 */
package com.pms.discount.service.impl;

/**
 * 
 */
import java.math.BigDecimal;
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
import com.pms.discount.dto.DiscountDTO;
import com.pms.discount.entity.Discount;
import com.pms.discount.repository.DiscountRepository;
import com.pms.discount.service.IDiscountService;
import com.pms.exception.ResourceNotFoundException;
import com.pms.guestdetails.GuestDetails;
import com.pms.guestdetails.dao.impl.GuestDetailsRepository;
import com.pms.personaldetails.PersonalDetails;
import com.pms.security.configuration.HotelContext;
import com.pms.security.service.BaseHotelService;
import com.pms.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl extends BaseHotelService implements IDiscountService {

    private final DiscountRepository repository;
    
    private final GuestDetailsRepository guestDetailsRepository;
    
    @Autowired
	private SoftDeleteService softDeleteService;
    
    @Autowired
	private AuditLogRepository auditRepo;

    @Auditable(action = "CREATE", entity = "DISCOUNTS")
    @Override
    public DiscountDTO createDiscount(DiscountDTO dto) {
    	
        validate(dto);

        Discount discount = mapToEntity(dto);
        
        assignHotel(discount, discount.getHotelId());

        return mapToDto(repository.save(discount));
    }

    @Auditable(action = "UPDATE", entity = "DISCOUNTS")
    @Override
    public DiscountDTO updateDiscount(Long id, DiscountDTO dto) {

        validate(dto);

        Discount discount = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found"));
        
        Long hotelId = HotelContext.getHotelId();
		validateHotelAccess(hotelId);
        
        GuestDetails guestDetails = guestDetailsRepository.findById(dto.getGuestDetailsId())
                .orElseThrow(() -> new RuntimeException("Guest not found with id: " + dto.getGuestDetailsId()));

        discount.setGuestDetails(guestDetails);

        discount.setDiscountName(dto.getDiscountName());
        discount.setPercentage(dto.getPercentage());
        discount.setFixedDiscount(dto.getFixedDiscount());
        discount.setDiscountDatetime(dto.getDiscountDatetime());
        discount.setRemarks(dto.getRemarks());
        discount.setIsActive(dto.getIsActive());

        return mapToDto(repository.save(discount));
    }

    @Override
    public DiscountDTO getByDiscountId(Long id) {
    	
    	Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);

        return mapToDto(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found")));
    }

    @Override
    public List<DiscountDTO> getAllDiscount() {
    	
    	Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);
        return repository.findByHotelIdAndIsDeletedFalseAndIsActiveTrue(hotelId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Auditable(action = "DELETE", entity = "DISCOUNTS")
    @Override
    public Boolean deleteDiscount(Long id) {
    	
    	Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
    	
    	validateHotelAccess(hotelId);
    	Discount entity = repository.findByIdAndHotelIdAndIsDeletedFalse(id, hotelId)
				.orElseThrow(() -> new RuntimeException("Record not found"));
    	
    	Discount b= softDeleteService.softDelete(id, repository);
    	
    	// ✅ Audit log
    			AuditLog log = new AuditLog();
    			log.setAction("DELETE");
    			log.setEntityName("DISCOUNTS");
    			log.setEntityId(entity.getId().toString());
    			log.setUsername(SecurityUtils.getCurrentUsername());
    			log.setTimestamp(LocalDateTime.now());
    			log.setOldValue(AuditUtil.toJson(entity));
    			 log.setBusinessTraceId(BusinessTraceContext.get());
    	         log.setRequestTraceId(RequestTraceContext.get());
    			auditRepo.save(log);
    			return b!=null ? true:false;

    }

    private void validate(DiscountDTO dto) {

        boolean percentage =
                dto.getPercentage() != null &&
                dto.getPercentage().compareTo(BigDecimal.ZERO) > 0;

        boolean fixed =
                dto.getFixedDiscount() != null &&
                dto.getFixedDiscount().compareTo(BigDecimal.ZERO) > 0;

        if (percentage == fixed) {
            throw new RuntimeException(
                    "Provide either Percentage or Fixed Discount");
        }

        if (percentage &&
                dto.getPercentage().compareTo(new BigDecimal("100")) > 0) {
            throw new RuntimeException(
                    "Percentage cannot exceed 100");
        }
    }

    private DiscountDTO mapToDto(Discount discount) {

        DiscountDTO dto = new DiscountDTO();
        
        Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}

        dto.setId(discount.getId());
        dto.setDiscountName(discount.getDiscountName());
        dto.setPercentage(discount.getPercentage());
        dto.setFixedDiscount(discount.getFixedDiscount());
        dto.setDiscountDatetime(discount.getDiscountDatetime());
        dto.setRemarks(discount.getRemarks());
        dto.setIsActive(discount.getIsActive());
        dto.setIsDeleted(discount.getIsDeleted());
        dto.setHotelId(hotelId);
        if(discount.getGuestDetails() != null) {
        	dto.setGuestDetailsId(discount.getGuestDetails().getId());
        } 
        return dto;
    }

    private Discount mapToEntity(DiscountDTO dto) {
    	
    	Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		
		GuestDetails guestDetails = guestDetailsRepository.findById(dto.getGuestDetailsId())
		        .orElseThrow(() -> new RuntimeException(
		                "Guest not found with id: " + dto.getGuestDetailsId()));

		Discount discount = Discount.builder()
                .discountName(dto.getDiscountName())
                .percentage(dto.getPercentage())
                .fixedDiscount(dto.getFixedDiscount())
                .discountDatetime(dto.getDiscountDatetime())
                .remarks(dto.getRemarks())
                .isActive(dto.getIsActive())
                .guestDetails(guestDetails)
                .build();
        
        discount.setHotelId(dto.getHotelId());
        discount.setIsDeleted(dto.getIsDeleted());
        return discount;
    }
    
    @Override
    public List<DiscountDTO> getDiscountsByGuestId(Long guestId) {

    	
    	Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);
        return repository.findByGuestDetailsId(guestId)
                .stream()
                .map(discount -> {

                    DiscountDTO dto = new DiscountDTO();

                    dto.setId(discount.getId());
                    dto.setDiscountName(discount.getDiscountName());
                    dto.setPercentage(discount.getPercentage());
                    dto.setFixedDiscount(discount.getFixedDiscount());
                    dto.setDiscountDatetime(discount.getDiscountDatetime());
                    dto.setRemarks(discount.getRemarks());
                    dto.setHotelId(discount.getHotelId());
                    dto.setIsActive(discount.getIsActive());
                    dto.setIsDeleted(discount.getIsDeleted());

                    if (discount.getGuestDetails() != null) {
                        dto.setGuestDetailsId(discount.getGuestDetails().getId());
                    }

                    return dto;
                })
                .toList();
    }
}
