/**
 * 
 */
package com.pms.dnr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pms.dnr.dto.CreateDnrRequestDTO;
import com.pms.dnr.dto.DnrValidationResponseDTO;
import com.pms.dnr.dto.UpdateDnrGuestRequestDTO;
import com.pms.dnr.entity.DnrGuest;
import com.pms.dnr.service.IDnrService;

/**
 * 
 */
@RestController
public class DnrGuestController {

	@Autowired
	private IDnrService service;

	@PostMapping("/user/createguestdnr")
	public DnrGuest create(@RequestBody CreateDnrRequestDTO dto) {
		return service.create(dto);
	}

	@GetMapping("/user/validateguestdnr/{personalDetailsId}")
	public ResponseEntity<DnrValidationResponseDTO> validateGuest(@PathVariable Long personalDetailsId) {

		return ResponseEntity.ok(service.validateGuest(personalDetailsId));
	}

	@PutMapping("/update/{dnrId}")
	public ResponseEntity<DnrGuest> updateGuestDnr(@PathVariable Long dnrId,
			@RequestBody UpdateDnrGuestRequestDTO dto) {

		return ResponseEntity.ok(service.updateGuestDnr(dnrId, dto));
	}
	
	@GetMapping("/user/list")
	public ResponseEntity<List<DnrGuest>> listAllDnrGuest() {

	    return ResponseEntity.ok(service.listAllDnrGuest());
	}

}
