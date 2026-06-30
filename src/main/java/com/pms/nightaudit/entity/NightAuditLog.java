/**
 * 
 */
package com.pms.nightaudit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pms.baseentity.BaseEntity;

@Entity
@Table(name = "night_audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NightAuditLog extends BaseEntity{

	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private LocalDate businessDate;

	    private Integer totalGuests;

	    private Double totalRoomRevenue;

	    private Double totalTax;

	    private Double totalRevenue;

	    private String status;

	    @Lob
	    private String errorMessage;

	    private LocalDateTime auditStartTime;

	    private LocalDateTime auditEndTime;

}