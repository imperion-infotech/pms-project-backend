/**
 * 
 */
package com.pms.nightaudit.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pms.nightaudit.entity.FolioTransaction;

/**
 * 
 */
@Repository
public interface FolioTransactionRepository
        extends JpaRepository<FolioTransaction, Long> {

    boolean existsByGuestIdAndBusinessDateAndTransactionTypeAndIsDeletedFalse(
            Long guestId,
            LocalDate businessDate,
            String transactionType);

    @Query("""
    SELECT COALESCE(SUM(f.amount),0)
    FROM FolioTransaction f
    WHERE f.hotelId = :hotelId
    AND f.businessDate = :businessDate
    AND f.isDeleted = false
    """)
    Double getTotalRevenue(
            Long hotelId,
            LocalDate businessDate);
}