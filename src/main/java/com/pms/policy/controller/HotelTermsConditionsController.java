/**
 * 
 */
package com.pms.policy.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pms.policy.dto.*;
import com.pms.policy.entity.HotelTermsConditionsHistory;
import com.pms.policy.enums.PolicyType;
import com.pms.policy.service.HotelTermsConditionsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HotelTermsConditionsController {

	private final HotelTermsConditionsService service;

	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'TERMS_CREATE')")	
	@PostMapping("/admin/createterms")
	public ResponseEntity<HotelTermsConditionsResponseDTO> create(@RequestBody HotelTermsConditionsRequestDTO dto) {

		return ResponseEntity.ok(service.create(dto));
	}

	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'TERMS_UPDATE')")
	@PutMapping("/admin/updateterms/{id}")
	public ResponseEntity<HotelTermsConditionsResponseDTO> update(@PathVariable Long id,@RequestBody HotelTermsConditionsRequestDTO dto) {

		return ResponseEntity.ok(service.update(id, dto));
	}

	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'TERMS_VIEW')")
	@GetMapping("/user/getterms/{id}")
	public ResponseEntity<HotelTermsConditionsResponseDTO> getById(@PathVariable Long id) {

		return ResponseEntity.ok(service.getById(id));
	}

	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'TERMS_VIEW')")
	@GetMapping("/user/getallterms")
	public ResponseEntity<Page<HotelTermsConditionsResponseDTO>> getAll(@RequestParam Long hotelId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "createdOn") String sortBy,
			@RequestParam(defaultValue = "desc") String sortDir) {

		return ResponseEntity.ok(service.getAll(hotelId, page, size, sortBy, sortDir));
	}

	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'TERMS_SEARCH')")
	@GetMapping("/user/termssearch")
	public ResponseEntity<Page<HotelTermsConditionsResponseDTO>> search(@RequestParam Long hotelId,
			@RequestParam String keyword, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		return ResponseEntity.ok(service.search(hotelId, keyword, page, size));
	}

	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'TERMS_VIEW')")
	@GetMapping("/admin/policy-type")
	public ResponseEntity<Page<HotelTermsConditionsResponseDTO>> getByPolicyType(@RequestParam Long hotelId,
			@RequestParam PolicyType policyType, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		return ResponseEntity.ok(service.getByPolicyType(hotelId, policyType, page, size));
	}

	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'TERMS_DELETE')")
	@DeleteMapping("/admin/deleteterms/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {

		service.softDelete(id);

		return ResponseEntity.ok("Policy deleted successfully");
	}

	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'TERMS_UPDATE')")
	@PatchMapping("/admin/changestatus/{id}/status")
	public ResponseEntity<HotelTermsConditionsResponseDTO> changeStatus(@PathVariable Long id,
			@RequestParam Boolean status) {

		return ResponseEntity.ok(service.changeStatus(id, status));
	}

	/*@PreAuthorize("@permissionChecker.hasPermission(authentication, 'TERMS_VIEW')")
	@GetMapping("/admin/history/{id}")
	public ResponseEntity<List<HotelTermsConditionsHistory>> getHistory(@PathVariable Long id) {

		return ResponseEntity.ok(service.getHistory(id));
	}

//	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'TERMS_UPDATE')")
	@PostMapping(value = "/admin/upload-pdf/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadPdf(@PathVariable Long id, @RequestParam MultipartFile file) {

		return ResponseEntity.ok(service.uploadPdf(id, file));
	}*/
}