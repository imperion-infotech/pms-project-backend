/**
 * 
 */
package com.pms.imageupload.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pms.imageupload.IFileStorageService;
import com.pms.util.ConstantUtils;

import io.swagger.v3.oas.annotations.Operation;

@RestController

public class ImageController {
	
	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final IFileStorageService fileStorageService;

    public ImageController(IFileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    // Upload image
    @PostMapping(value ="/user/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload image")
//    @PostMapping("/auth/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,@RequestParam("imageType") String  imageType) {
        try {
        	String fileName = fileStorageService.storeFile(file,imageType);
            return ResponseEntity.ok("Image uploaded successfully: " + fileName);
        } catch (IOException e) {
        	logger.error("Exception in controller of uploadImage api :"+e.getMessage() );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Upload failed: " + e.getMessage());
        }
    }

    // Serve image
    @GetMapping("/user/{fileName:.+}")
//    @GetMapping("/auth/{fileName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName,@RequestParam("imageType") String  imageType) {
        try {
            Path filePath = fileStorageService.loadFile(fileName,imageType);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // You can detect type dynamically
                    .body(resource);

        } catch (MalformedURLException e) {
        	logger.error("Exception in controller of getImage api :"+e.getMessage() );
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * DELETE endpoint to remove an image by filename
     * Example: DELETE /api/images/delete/sample.jpg
     */
    @DeleteMapping("/user/delete/{filename:.+}")
    public ResponseEntity<String> deleteImage(@PathVariable String filename,@RequestParam("imageType") String  imageType) {
        try {
            // Prevent directory traversal attacks
        	Path filePath = fileStorageService.loadFile(filename,imageType);
            if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid filename.");
            }

//            String fileUrl = ConstantUtils.IMAGE_UPLOAD_PATH ;
            
//            Path filePath = Paths.get(fileUrl).resolve(filename).normalize();
            File file = filePath.toFile();

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Image not found: " + filename);
            }

            // Delete the file
            Files.delete(filePath);

            return ResponseEntity.ok("Image deleted successfully: " + filename);

        } catch (Exception e) {
        	logger.error("Exception in controller of deleteImage api :"+e.getMessage() );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting image: " + e.getMessage());
        }
    }
    
    
    
}
