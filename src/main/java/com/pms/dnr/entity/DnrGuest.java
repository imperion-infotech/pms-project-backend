/**
 * 
 */
package com.pms.dnr.entity;

import java.time.LocalDateTime;

import com.pms.baseentity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "dnr_guest")
public class DnrGuest  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "personal_details_id", nullable = false)
    private Long personalDetailsId;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "dnr_status")
    private Boolean dnrStatus = true;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private Long hotelId;

    @Column(name = "request_trace_id")
    private String requestTraceId;

    @Column(name = "business_trace_id")
    private String businessTraceId;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

}
