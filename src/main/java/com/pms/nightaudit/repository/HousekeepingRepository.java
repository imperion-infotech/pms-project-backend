/**
 * 
 */
package com.pms.nightaudit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.nightaudit.entity.HousekeepingStatus;

/**
 * 
 */
public interface HousekeepingRepository extends JpaRepository<HousekeepingStatus, Long> {

}
