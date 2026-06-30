/**
 * 
 */
package com.pms.tax.controller;

/**
 * 
 */
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.tax.dto.GuestTaxTransactionResponse;
import com.pms.tax.service.IGuestTaxTransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/guest-tax")
@RequiredArgsConstructor
public class GuestTaxTransactionController {

	private final IGuestTaxTransactionService service;

	@PostMapping("/generate/{bookingId}")
	public ResponseEntity<String> generateTax(@PathVariable Long bookingId) {

		service.saveGuestTaxes(bookingId);

		return ResponseEntity.ok("Guest tax transactions generated successfully");
	}
	
	 @PutMapping("/update/{bookingId}")
	    public ResponseEntity<String> updateGuestTaxes(
	            @PathVariable Long bookingId) {

	        service.updateGuestTaxes(bookingId);

	        return ResponseEntity.ok(
	                "Guest taxes updated successfully for booking id : "
	                        + bookingId);
	    }

	@GetMapping("/{bookingId}")
	public ResponseEntity<List<GuestTaxTransactionResponse>> getTaxes(@PathVariable Long bookingId) {

		return ResponseEntity.ok(service.getTaxesByBooking(bookingId));
	}
}