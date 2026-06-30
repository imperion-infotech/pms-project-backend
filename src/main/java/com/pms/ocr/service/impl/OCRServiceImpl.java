/**
 * 
 */
package com.pms.ocr.service.impl;

import java.util.Map;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.pms.ocr.dto.OCRGuestResponseDTO;
import com.pms.ocr.service.OCRService;

@Service
public class OCRServiceImpl implements OCRService {

	private final RestTemplate restTemplate = new RestTemplate();

	@Override
	public OCRGuestResponseDTO scanDocument(MultipartFile file, String imageType) {

		try {

			/*
			 * OCR API URL
			 */
			String apiUrl = "https://api.example.com/ocr";

			/*
			 * HEADERS
			 */
			HttpHeaders headers = new HttpHeaders();

			headers.setContentType(MediaType.APPLICATION_JSON);

			headers.setBearerAuth("YOUR_API_KEY");

			/*
			 * REQUEST BODY (Base64/image upload depends on OCR provider)
			 */
			HttpEntity<String> request = new HttpEntity<>("{}", headers);

			/*
			 * CALL OCR API
			 */
			ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Map.class);

			/*
			 * PARSE RESPONSE
			 */
			Map body = response.getBody();

			OCRGuestResponseDTO dto = new OCRGuestResponseDTO();

			dto.setFirstName((String) body.get("firstName"));

			dto.setLastName((String) body.get("lastName"));

			dto.setDocumentNumber((String) body.get("documentNumber"));

			dto.setDateOfBirth((String) body.get("dob"));

			dto.setNationality((String) body.get("nationality"));

			dto.setGender((String) body.get("gender"));

			dto.setAddress((String) body.get("address"));

			dto.setExpiryDate((String) body.get("expiryDate"));

			dto.setDocumentType((String) body.get("documentType"));

			return dto;

		} catch (Exception e) {

			throw new RuntimeException("OCR scan failed : " + e.getMessage());
		}
	}
}