/**
 * 
 */
package com.pms.guestprofile;

import java.time.LocalDate;

import com.pms.personaldetails.PersonalDetails;
import com.pms.personaldetails.controller.ContactInformationTypeEnum;

import lombok.Data;

/**
 * 
 */
@Data
public class PersonalInfoDto {
    private String firstName;
    private String lastName;
    private String companyName;
    private String phone;
    private String email;
    private String address;
    private String profilePhoto;
    private String signature;
    private String folioNo;
    private String crsFolioNo;
    private String businessTraceId;
    private ContactInformationTypeEnum contactInformationTypeEnum;
    private LocalDate birthDate;
    private String nationality;
    
    
    public static PersonalInfoDto fromEntity(PersonalDetails entity) {
        if (entity == null) {
            return null;
        }

        PersonalInfoDto dto = new PersonalInfoDto();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setCompanyName(entity.getCompanyName());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        dto.setProfilePhoto(entity.getProfilePhoto());
        dto.setSignature(entity.getSignature());
        dto.setFolioNo(entity.getFolioNo());
        dto.setCrsFolioNo(entity.getCrsFolioNo());
        dto.setBusinessTraceId(entity.getBusinessTraceId());
        dto.setContactInformationTypeEnum(entity.getContactInformationTypeEnum());
        dto.setBirthDate(entity.getBirthDate());
        dto.setNationality(entity.getNationality());

        return dto;
    }
}