/**
 * 
 */
package com.pms.tax.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.tax.entity.GuestTaxTransaction;

public interface GuestTaxTransactionRepository
        extends JpaRepository<GuestTaxTransaction, Long> {

    List<GuestTaxTransaction> findByBookingId(Long bookingId);
    
    List<GuestTaxTransaction> findByBookingIdAndIsDeletedFalse(Long bookingId);
}