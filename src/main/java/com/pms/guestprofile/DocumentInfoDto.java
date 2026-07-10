/**
 * 
 */
package com.pms.guestprofile;

import java.util.Date;

import com.pms.document.entity.DocumentDetails;

import lombok.Data;

@Data
public class DocumentInfoDto {
    private String documentNumber;
    private Date validTill;
    private String frontImagePath;
    private String backImagePath;
    private String remark;

    public static DocumentInfoDto fromEntity(DocumentDetails entity) {
        if (entity == null) return null;

        DocumentInfoDto dto = new DocumentInfoDto();
        dto.setDocumentNumber(entity.getDocumentNumber());
        dto.setValidTill(entity.getValidTill());
        dto.setFrontImagePath(entity.getFrontImagePath());
        dto.setBackImagePath(entity.getBackImagePath());
        dto.setRemark(entity.getRemark());
        return dto;
    }
}