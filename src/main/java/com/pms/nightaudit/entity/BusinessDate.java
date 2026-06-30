package com.pms.nightaudit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pms.baseentity.BaseEntity;
import com.pms.hotel.entity.Hotel;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "business_date")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDate businessDate;

    private Boolean auditRunning = false;
    
    private LocalDateTime lastAuditAt;
    
    private Boolean currentBusinessDate = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", insertable = false, updatable = false)
    private Hotel hotel;

}