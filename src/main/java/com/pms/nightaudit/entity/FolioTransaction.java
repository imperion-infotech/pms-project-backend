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

@Entity
@Table(name = "folio_transaction")
@Getter
@Setter
public class FolioTransaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long guestId;

    private Long rentDetailsId;

    private LocalDate businessDate;

    private String transactionType;

    private Double amount;

    private String remarks;
}