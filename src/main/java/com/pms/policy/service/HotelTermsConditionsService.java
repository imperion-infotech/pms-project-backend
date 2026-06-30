package com.pms.policy.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.pms.policy.dto.HotelTermsConditionsRequestDTO;
import com.pms.policy.dto.HotelTermsConditionsResponseDTO;
import com.pms.policy.entity.HotelTermsConditionsHistory;
import com.pms.policy.enums.PolicyType;

public interface HotelTermsConditionsService {

	HotelTermsConditionsResponseDTO create(HotelTermsConditionsRequestDTO dto);

	HotelTermsConditionsResponseDTO update(Long id, HotelTermsConditionsRequestDTO dto);

	HotelTermsConditionsResponseDTO getById(Long id);

	Page<HotelTermsConditionsResponseDTO> getAll(Long hotelId, int page, int size, String sortBy, String sortDir);

	Page<HotelTermsConditionsResponseDTO> search(Long hotelId, String keyword, int page, int size);

	Page<HotelTermsConditionsResponseDTO> getByPolicyType(Long hotelId, PolicyType policyType, int page, int size);

	void softDelete(Long id);

	HotelTermsConditionsResponseDTO changeStatus(Long id, Boolean status);

	List<HotelTermsConditionsHistory> getHistory(Long termsConditionId);

	String uploadPdf(Long termsConditionId, MultipartFile file);
}