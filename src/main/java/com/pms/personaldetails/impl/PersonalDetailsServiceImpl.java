/**
 * 
 */
package com.pms.personaldetails.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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
import com.pms.personaldetails.service.IPersonalDetailsService;
import com.pms.search.specification.PersonalDetailsSpecification;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.service.BaseHotelService;
import com.pms.security.util.SecurityUtils;
import com.pms.util.TraceIdGenerator;

import jakarta.transaction.Transactional;

/**
 * 
 */
@Service
public class PersonalDetailsServiceImpl extends BaseHotelService implements IPersonalDetailsService {

	@Autowired
	private PersonalDetailsRepository repository;
	@Autowired
	private AuditLogRepository auditRepo;

    private static final String BUSINESS_TRACE_HEADER = "X-Business-Trace-Id";

	public PersonalDetailsServiceImpl(PersonalDetailsRepository repository, AuditLogRepository auditRepo) {
		super();
		this.repository = repository;
		this.auditRepo = auditRepo;
	}

	@Autowired
	private SoftDeleteService softDeleteService;

	public List<PersonalDetails> getAll() {

//	        return repository.findAll();
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);
		if (isSuperAdmin())
			return repository.findByIsDeletedFalse();
		else
			return repository.findByHotelIdAndIsDeletedFalse(hotelId);

	}

	/*
	 * public PersonalDetails getById(Long id) { return repository.findById(id)
	 * .orElseThrow(() -> new RuntimeException("Record not found with id " + id)); }
	 */

	public PersonalDetails getById(Long id) {
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);
		return repository.findByIdAndHotelIdAndIsDeletedFalse(id, hotelId)
				.orElseThrow(() -> new RuntimeException("Record not found"));
	}

	@Auditable(action = "CREATE", entity = "PERSONALDETAILS")
	public PersonalDetails create(PersonalDetails details) {
	    
		Long userId = UserContext.getUserId();

		if (userId == null) {
			throw new ResourceNotFoundException("User not selected");
		}
		
		String businessTraceId = TraceIdGenerator.generateBusinessTraceId();
	    BusinessTraceContext.set(businessTraceId);
	    details.setBusinessTraceId(businessTraceId);
	    
		assignHotel(details, details.getHotelId());
		details.setCreatedBy(userId);
		return repository.save(details);

	}

	@Auditable(action = "UPDATE", entity = "PERSONALDETAILS")
	public PersonalDetails update(Long id, PersonalDetails details) {
		Long userId = UserContext.getUserId();
		if (userId == null) {
			throw new ResourceNotFoundException("User not selected");
		}
		Long hotelId = HotelContext.getHotelId();
		validateHotelAccess(hotelId);
		PersonalDetails existing = getById(id);
		existing.setFirstName(details.getFirstName());
		existing.setLastName(details.getLastName());
		existing.setCompanyName(details.getCompanyName());
		existing.setEmail(details.getEmail());
		existing.setPhone(details.getPhone());
		existing.setAddress(details.getAddress());
		existing.setContactInformationTypeEnum(details.getContactInformationTypeEnum());
		existing.setUpdatedBy(userId);
		existing.setUpdatedOn(LocalDateTime.now());
		existing.setBirthDate(details.getBirthDate());
		existing.setNationality(details.getNationality());
		existing.setProfilePhoto(details.getProfilePhoto());
		existing.setSignature(details.getSignature());
		BusinessTraceContext.set(existing.getBusinessTraceId());
		return repository.save(existing);
	}

	/*
	 * @Auditable(action = "DELETE", entity = "PERSONALDETAILS") public boolean
	 * delete(Long id) { repository.deleteById(id); return true; }
	 */

	@Auditable(action = "DELETE", entity = "PERSONALDETAILS")
	@Transactional
	public Boolean deletePersonalDetails(Long id) {
		
		
		Long hotelId = HotelContext.getHotelId();
		Long userId = UserContext.getUserId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);
		PersonalDetails entity = repository.findByIdAndHotelIdAndIsDeletedFalse(id, hotelId)
				.orElseThrow(() -> new RuntimeException("Record not found"));
		BusinessTraceContext.set(entity.getBusinessTraceId());
//	            // ✅ Soft delete
//	            entity.setDeleted(true);
//	            entity.setDeletedOn(LocalDateTime.now());
//	            entity.setDeletedBy(userId);
//
//	            repository.save(entity);
		PersonalDetails b= softDeleteService.softDelete(id, repository);
		
		// ✅ Audit log
		AuditLog log = new AuditLog();
		log.setAction("DELETE");
		log.setEntityName("PERSONAL DETAILS");
		log.setEntityId(entity.getId().toString());
		log.setUsername(SecurityUtils.getCurrentUsername());
		log.setTimestamp(LocalDateTime.now());
		log.setOldValue(AuditUtil.toJson(entity));
		 log.setBusinessTraceId(BusinessTraceContext.get());
         log.setRequestTraceId(RequestTraceContext.get());
		auditRepo.save(log);
		return b!=null ? true:false;
	}

	public List<PersonalDetails> search(String firstName, String lastName, String email, String phone, String address,String nationality) {
		Long hotelId = HotelContext.getHotelId(); // 🔥 get from JWT
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
	     validateHotelAccess(hotelId);
		Specification<PersonalDetails> spec = Specification.where(PersonalDetailsSpecification.hasHotelId(hotelId))
				.and(PersonalDetailsSpecification.isNotDeleted()) // ✅ ADD THIS
				.and(PersonalDetailsSpecification.hasFirstName(firstName))
				.and(PersonalDetailsSpecification.hasLastName(lastName))
				.and(PersonalDetailsSpecification.hasNationality(nationality))
				.and(PersonalDetailsSpecification.hasEmail(email)).and(PersonalDetailsSpecification.hasPhone(phone))
				.and(PersonalDetailsSpecification.hasAddress(address));

		return repository.findAll(spec);
	}

}
