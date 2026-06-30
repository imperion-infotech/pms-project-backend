/**
 * 
 */
package com.pms.imageupload;

import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.io.IOException;

/**
 * 
 */
public interface IFileStorageService {
	
	public String storeFile(MultipartFile file,String imageType) throws IOException, java.io.IOException;
	 public Path loadFile(String fileName,String imageType);

}
