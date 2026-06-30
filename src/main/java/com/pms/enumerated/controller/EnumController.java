/**
 * 
 */
package com.pms.enumerated.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.document.entity.DocumentTypeEnum;
import com.pms.enumerated.dto.EnumResponse;
import com.pms.personaldetails.controller.ContactInformationTypeEnum;

/**
 * 
 */
@RestController
public class EnumController {

//    @GetMapping("/admin/getdocumenttypes")
//    public DocumentTypeEnum[] getAllDocumentType() {
//        return DocumentTypeEnum.values();
//    }
    
    @GetMapping("/admin/getdocumenttypes")
    public List<EnumResponse> getAllDocumentType() {
        return Arrays.stream(DocumentTypeEnum.values())
                .map(status -> new EnumResponse(
                        status.name(),
                        status.getDescription()
                ))
                .collect(Collectors.toList());
    }
    
    @GetMapping("/user/getcontactinformationtype")
    public List<EnumResponse> getAllContactInformationType() {
        return Arrays.stream(ContactInformationTypeEnum.values())
                .map(status -> new EnumResponse(
                        status.name(),
                        status.getDescription()
                ))
                .collect(Collectors.toList());
    }
}
