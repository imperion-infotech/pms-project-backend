/**
 * 
 */
package com.pms.policy.entity;

import java.time.LocalDateTime;

import com.pms.policy.enums.PolicyType;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hotel_terms_conditions_history")
@Getter
@Setter
public class HotelTermsConditionsHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "terms_condition_id")
    private Long termsConditionId;

    @Column(name = "hotel_id")
    private Long hotelId;

    @Enumerated(EnumType.STRING)
    @Column(name = "policy_type")
    private PolicyType policyType;

    @Column(name = "language_code")
    private String languageCode;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "version_no")
    private Integer versionNo;

    @Column(name = "pdf_file_name")
    private String pdfFileName;

    @Column(name = "pdf_file_path")
    private String pdfFilePath;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;
}