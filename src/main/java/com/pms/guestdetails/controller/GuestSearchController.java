/**
 * 
 */
package com.pms.guestdetails.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.guestdetails.dto.GuestSearchRequestDTO;
import com.pms.guestdetails.dto.GuestSearchResponseDTO;
import com.pms.guestdetails.service.IGuestSearchService;

@RestController
@RequestMapping("/api/guest-search")
public class GuestSearchController {

	@Autowired
	private IGuestSearchService service;

	@PostMapping("/user/searchguest")
	public ResponseEntity<List<GuestSearchResponseDTO>> searchGuests(@RequestBody GuestSearchRequestDTO request) {

		return ResponseEntity.ok(service.searchGuests(request));
	}
}
