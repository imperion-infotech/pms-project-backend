/**
 * 
 */
package com.pms.nightaudit.entity;

import java.time.LocalDate;

import com.pms.baseentity.BaseEntity;

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
@Entity
@Table(name = "housekeeping_status")
@Getter
@Setter
public class HousekeepingStatus extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roomId;

    private String roomStatus;

    private String housekeepingStatus;

    private LocalDate businessDate;
}