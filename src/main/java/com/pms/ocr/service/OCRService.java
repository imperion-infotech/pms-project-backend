/**
 * 
 */
package com.pms.ocr.service;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.pms.ocr.dto.OCRGuestResponseDTO;

public interface OCRService {

    OCRGuestResponseDTO scanDocument(MultipartFile file,String  imageType);
}