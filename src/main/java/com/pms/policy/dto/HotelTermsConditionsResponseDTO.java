/**
 * 
 */
package com.pms.policy.dto;

import java.time.LocalDateTime;

import com.pms.policy.enums.PolicyType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HotelTermsConditionsResponseDTO {

    private Long termsConditionId;

    private Long hotelId;

    private PolicyType policyType;

    private String languageCode;

    private String title;

    private String content;

    private Integer versionNo;

    private String pdfFileName;

    private Boolean isActive;

    private Boolean isDeleted;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;
}