/**
 * 
 */
package com.pms.nightaudit.entity;

/**
 * 
 */
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pms.baseentity.BaseEntity;

@Entity
@Table(name = "daily_revenue_summary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyRevenueSummary extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate businessDate;

	private BigDecimal roomRevenue = BigDecimal.ZERO;

	private BigDecimal taxRevenue = BigDecimal.ZERO;

	private BigDecimal otherRevenue = BigDecimal.ZERO;

	private BigDecimal totalRevenue = BigDecimal.ZERO;

}