/**
 * 
 */
package com.pms.ocr.dto;

/**
 * 
 */
import lombok.Data;

@Data
public class OCRGuestResponseDTO {

    private String firstName;

    private String lastName;

    private String fullName;

    private String documentNumber;

    private String nationality;

    private String gender;

    private String address;

    private String dateOfBirth;

    private String expiryDate;

    private String documentType;
}