/**
 * 
 */
package com.pms.roomhistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pms.roomhistory.entity.RoomActivityHistory;

@Repository
public interface RoomActivityHistoryRepository extends JpaRepository<RoomActivityHistory, Long> {

}