/**
 * 
 */
package com.pms.discount.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pms.baseentity.BaseEntity;
import com.pms.guestdetails.GuestDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "discounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discount extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String discountName;

    @Column(precision = 5, scale = 2)
    private BigDecimal percentage;

    @Column(name = "fixed_discount", precision = 10, scale = 2)
    private BigDecimal fixedDiscount;

    private LocalDateTime discountDatetime;

    @Column(name = "remarks")
    private String remarks;

    private Boolean isActive = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_details_id", nullable = false)
    private GuestDetails guestDetails;
}
