package com.pms.policy.entity;

import com.pms.baseentity.BaseEntity;
import com.pms.policy.enums.PolicyType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hotel_terms_conditions")
@Getter
@Setter
public class HotelTermsConditions extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_condition_id")
    private Long termsConditionId;

    /*
     * HOTEL REFERENCE
     */
    @Column(name = "hotel_id", nullable = false)
    private Long hotelId;

    /*
     * POLICY TYPE
     * Example:
     * CHECKIN_POLICY
     * CANCELLATION_POLICY
     * REFUND_POLICY
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "policy_type", nullable = false)
    private PolicyType policyType;

    /*
     * LANGUAGE SUPPORT
     * Example:
     * en = English
     * hi = Hindi
     * ar = Arabic
     */
    @Column(name = "language_code")
    private String languageCode = "en";

    /*
     * POLICY TITLE
     */
    @Column(name = "title")
    private String title;

    /*
     * HTML / RICH TEXT CONTENT
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /*
     * VERSIONING
     */
    @Column(name = "version_no")
    private Integer versionNo = 1;

    /*
     * PDF ATTACHMENT DETAILS
     */
    @Column(name = "pdf_file_name")
    private String pdfFileName;

    @Column(name = "pdf_file_path")
    private String pdfFilePath;

    /*
     * ACTIVE / INACTIVE
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

    /*
     * SOFT DELETE
     */
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}