/**
 * 
 */
package com.pms.ocr.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pms.ocr.dto.OCRGuestResponseDTO;
import com.pms.ocr.service.OCRService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ocr")
@RequiredArgsConstructor
public class OCRController {

    private final OCRService ocrService;

    @PostMapping("/scan-id")
    public ResponseEntity<OCRGuestResponseDTO> scanId(
            @RequestParam("file") MultipartFile file,@RequestParam("imageType") String  imageType) {

        return ResponseEntity.ok(
                ocrService.scanDocument(file,imageType));
    }
}