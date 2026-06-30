/**
 * 
 */
package com.pms.email.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.pms.email.dto.EmailRequestDTO;
import com.pms.email.service.IEmailService;

import jakarta.validation.Valid;

@RestController
public class EmailController {

	@Autowired
	private IEmailService emailService;

	// JSON-only endpoint (no attachments)
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'EMAIL_SEND')")
	@PostMapping(value = "/user/email/send")
	public ResponseEntity<?> sendEmail(@RequestBody @Valid EmailRequestDTO request) {

	    try {
	        emailService.sendEmail(
	            request.getTo(),
	            request.getCc(),
	            request.getBcc(),
	            request.getSubject(),
	            request.getText(),
	            null  // no attachments
	        );
	        return ResponseEntity.ok("Email sent successfully");
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("Failed to send email: " + e.getMessage());
	    }
	}

	// Multipart endpoint (with attachments)
	@PreAuthorize("@permissionChecker.hasPermission(authentication, 'EMAIL_SEND')")
	@PostMapping(value = "/user/email/send-with-attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> sendEmailWithAttachments(
	        @RequestPart("request") String requestJson,  // ← String, not DTO
	        @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

	    try {
	    	 ObjectMapper mapper = new ObjectMapper();
	         EmailRequestDTO request = mapper.readValue(requestJson, EmailRequestDTO.class);
	        List<File> attachmentFiles = new ArrayList<>();
	        if (attachments != null) {
	            for (MultipartFile mf : attachments) {
	                if (!mf.isEmpty()) {
	                    File temp = File.createTempFile("attachment_", "_" + mf.getOriginalFilename());
	                    mf.transferTo(temp);
	                    temp.deleteOnExit();
	                    attachmentFiles.add(temp);
	                }
	            }
	        }

	        emailService.sendEmail(
	            request.getTo(),
	            request.getCc(),
	            request.getBcc(),
	            request.getSubject(),
	            request.getText(),
	            attachmentFiles.isEmpty() ? null : attachmentFiles
	        );
	        return ResponseEntity.ok("Email sent successfully");
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("Failed to send email: " + e.getMessage());
	    }
	}
}