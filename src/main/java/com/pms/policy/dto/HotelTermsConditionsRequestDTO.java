/**
 * 
 */
package com.pms.policy.dto;

import com.pms.policy.enums.PolicyType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelTermsConditionsRequestDTO {

    private Long hotelId;

    private PolicyType policyType;

    private String languageCode;

    private String title;

    private String content;

    private Boolean isActive;
}