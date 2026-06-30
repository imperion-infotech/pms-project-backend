package com.pms.policy.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pms.auditlog.annotation.Auditable;
import com.pms.auditlog.context.BusinessTraceContext;
import com.pms.common.service.SoftDeleteService;
import com.pms.exception.ResourceNotFoundException;
import com.pms.policy.dto.HotelTermsConditionsRequestDTO;
import com.pms.policy.dto.HotelTermsConditionsResponseDTO;
import com.pms.policy.entity.HotelTermsConditions;
import com.pms.policy.entity.HotelTermsConditionsHistory;
import com.pms.policy.entity.PolicyDocument;
import com.pms.policy.enums.PolicyType;
import com.pms.policy.repository.HotelTermsConditionsHistoryRepository;
import com.pms.policy.repository.HotelTermsConditionsRepository;
import com.pms.policy.repository.PolicyDocumentRepository;
import com.pms.policy.service.HotelTermsConditionsService;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.service.AuthService;
import com.pms.security.service.BaseHotelService;
import com.pms.stay.entity.StayDetails;
import com.pms.util.ConstantUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HotelTermsConditionsServiceImpl extends BaseHotelService  implements HotelTermsConditionsService {

	private final HotelTermsConditionsRepository repository;
	private final HotelTermsConditionsHistoryRepository historyRepository;
	private final PolicyDocumentRepository documentRepository;
	
	@Autowired
	private SoftDeleteService softDeleteService;

	@Autowired
	private AuthService authService;
	
	private final String UPLOAD_DIR = ConstantUtils.IMAGE_UPLOAD_PATH +  "/termsandcondition"  ;

	@Auditable(action = "CREATE", entity = "TERMSANDCONDITION")
	@Override
	public HotelTermsConditionsResponseDTO create(HotelTermsConditionsRequestDTO dto) {

		HotelTermsConditions entity = new HotelTermsConditions();
		
		assignHotel(entity, dto.getHotelId());

	
		entity.setHotelId(authService.getCurrentHotelId());
		entity.setPolicyType(dto.getPolicyType());
		entity.setLanguageCode(dto.getLanguageCode());
		entity.setTitle(dto.getTitle());
		entity.setContent(dto.getContent());
		entity.setIsActive(dto.getIsActive());

		repository.save(entity);

		return mapToDTO(entity);
	}

	@Auditable(action = "UPDATE", entity = "TERMSANDCONDITION")
	@Override
	public HotelTermsConditionsResponseDTO update(Long id, HotelTermsConditionsRequestDTO dto) {

		HotelTermsConditions entity = repository.findByTermsConditionIdAndIsDeletedFalse(id);
		if (entity == null) {
			throw new RuntimeException("Policy not found");
		}
		
		validateHotelAccess(entity.getHotelId());


		saveHistory(entity);

		entity.setPolicyType(dto.getPolicyType());
		entity.setLanguageCode(dto.getLanguageCode());
		entity.setTitle(dto.getTitle());
		entity.setContent(dto.getContent());
		entity.setIsActive(dto.getIsActive());
		entity.setHotelId(dto.getHotelId());
		entity.setVersionNo(entity.getVersionNo() + 1);

		repository.save(entity);

		return mapToDTO(entity);
	}

	@Override
	public HotelTermsConditionsResponseDTO getById(Long id) {

		HotelTermsConditions entity = repository.findByTermsConditionIdAndIsDeletedFalse(id);

		if (entity == null) {
			throw new RuntimeException("Policy not found");
		}

		return mapToDTO(entity);
	}

	@Override
	public Page<HotelTermsConditionsResponseDTO> getAll(Long hotelId, int page, int size, String sortBy,
			String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, size, sort);

		return repository.findByHotelIdAndIsDeletedFalse(hotelId, pageable).map(this::mapToDTO);
	}

	@Override
	public Page<HotelTermsConditionsResponseDTO> search(Long hotelId, String keyword, int page, int size) {

		Pageable pageable = PageRequest.of(page, size);

		return repository.findByHotelIdAndTitleContainingIgnoreCaseAndIsDeletedFalse(hotelId, keyword, pageable)
				.map(this::mapToDTO);
	}

	@Override
	public Page<HotelTermsConditionsResponseDTO> getByPolicyType(Long hotelId, PolicyType policyType, int page,
			int size) {

		Pageable pageable = PageRequest.of(page, size);

		return repository.findByHotelIdAndPolicyType(hotelId, policyType, pageable)
				.map(this::mapToDTO);
	}

	@Auditable(action = "DELETE", entity = "TERMSANDCONDITION")
	@Override
	public void softDelete(Long id) {
		
		Long hotelId = HotelContext.getHotelId();
		Long userId = UserContext.getUserId();
		if (userId == null) {
			throw new ResourceNotFoundException("User not selected");
		}
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);

		HotelTermsConditions entity= softDeleteService.softDelete(id, repository);
		saveHistory(entity);
	}

	@Override
	public HotelTermsConditionsResponseDTO changeStatus(Long id, Boolean status) {
		
		Long hotelId = HotelContext.getHotelId();
		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}
		validateHotelAccess(hotelId);

		HotelTermsConditions entity = repository.findByTermsConditionIdAndIsDeletedFalse(id);

		entity.setIsActive(status);

		repository.save(entity);

		return mapToDTO(entity);
	}

	@Override
	public List<HotelTermsConditionsHistory> getHistory(Long termsConditionId) {

		return historyRepository.findByTermsConditionIdOrderByVersionNoDesc(termsConditionId);
	}

	@Override
	public String uploadPdf(Long termsConditionId, MultipartFile file) {

		try {

			Long hotelId = HotelContext.getHotelId();
			if (hotelId == null) {
				throw new ResourceNotFoundException("Hotel not selected");
			}
			validateHotelAccess(hotelId);
			
			if (!file.getContentType().equals("application/pdf")) {

				throw new RuntimeException("Only PDF files allowed");
			}

			File dir = new File(UPLOAD_DIR);

			if (!dir.exists()) {
				dir.mkdirs();
			}

			String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			System.out.println("Filename:::"+fileName);
			Path path = Paths.get(UPLOAD_DIR+"/termsandcondition" + fileName);

			Files.copy(file.getInputStream(), path);

			HotelTermsConditions entity = repository.findByTermsConditionIdAndIsDeletedFalse(termsConditionId);

			entity.setPdfFileName(fileName);
			entity.setPdfFilePath(path.toString());

			repository.save(entity);

			PolicyDocument document = new PolicyDocument();

			document.setTermsConditionId(termsConditionId);

			document.setFileName(fileName);

			document.setFilePath(path.toString());

			document.setUploadedOn(LocalDateTime.now());

			documentRepository.save(document);

			return "PDF uploaded successfully";

		} catch (IOException e) {

			throw new RuntimeException("File upload failed");
		}
	}

	private void saveHistory(HotelTermsConditions entity) {

		HotelTermsConditionsHistory history = new HotelTermsConditionsHistory();

		history.setTermsConditionId(entity.getTermsConditionId());

		history.setHotelId(entity.getHotelId());

		history.setPolicyType(entity.getPolicyType());

		history.setLanguageCode(entity.getLanguageCode());

		history.setTitle(entity.getTitle());

		history.setContent(entity.getContent());

		history.setVersionNo(entity.getVersionNo());

		history.setPdfFileName(entity.getPdfFileName());

		history.setPdfFilePath(entity.getPdfFilePath());

		history.setModifiedBy(entity.getUpdatedBy().toString());

		history.setModifiedOn(LocalDateTime.now());

		historyRepository.save(history);
	}

	private HotelTermsConditionsResponseDTO mapToDTO(HotelTermsConditions entity) {

		return HotelTermsConditionsResponseDTO.builder().termsConditionId(entity.getTermsConditionId())
				.hotelId(entity.getHotelId()).policyType(entity.getPolicyType()).languageCode(entity.getLanguageCode())
				.title(entity.getTitle()).content(entity.getContent()).versionNo(entity.getVersionNo())
				.pdfFileName(entity.getPdfFileName()).isActive(entity.getIsActive()).isDeleted(entity.getIsDeleted())
				.createdOn(entity.getCreatedOn()).updatedOn(entity.getUpdatedOn()).build();
	}
}