/**
 * 
 */
package com.pms.guestdetails.dao.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.pms.guestdetails.controller.GuestDetailsController;
import com.pms.guestdetails.dto.RoomActivityResponseDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Repository
@RequiredArgsConstructor
public class GuestDetailsRepositoryImpl implements GuestDetailsRepositoryCustom {
	
	private static final Logger logger = LoggerFactory.getLogger(GuestDetailsRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RoomActivityResponseDTO> getRoomActivities(Long hotelId,
            LocalDateTime fromDate,
            LocalDateTime toDate) {

        String sql = """
            SELECT
                latest.room_master_id,
                latest.guest_details_id,
                latest.guest_name,
                latest.check_in_date,
                latest.check_out_date,
                latest.guest_details_status,
                latest.personal_details_id,
                latest.created_on
            FROM (
                SELECT DISTINCT ON (gd.room_master_id)
                    gd.room_master_id,
                    gd.guest_details_id,
                    CONCAT(pd.first_name,' ',pd.last_name) AS guest_name,
                    gd.check_in_date,
                    gd.check_out_date,
                    gd.guest_details_status,
                    pd.id AS personal_details_id,
                    gd.created_on
                FROM guest_details gd
                LEFT JOIN personal_details pd
                    ON pd.id = gd.personal_details_id
                WHERE gd.hotel_id = :hotelId 
                and gd.created_on BETWEEN :fromDate AND :toDate
                ORDER BY gd.room_master_id,
                         gd.guest_details_id DESC
            ) latest
            ORDER BY latest.check_in_date
            """;

        Query query = entityManager.createNativeQuery(sql);

        query.setParameter("hotelId", hotelId);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);

        List<Object[]> rows = query.getResultList();

        List<RoomActivityResponseDTO> result = new ArrayList<>();

        for (Object[] row : rows) {
        	logger.info("Row: {}", Arrays.toString(row));
//        	Long roomMasterId = row[0] == null ? null : ((Number) row[0]).longValue();
			result.add(new RoomActivityResponseDTO(
					row[0] == null ? null : ((Number) row[0]).longValue(),
                    //((Number) row[0]).longValue(),
                    ((Number) row[1]).longValue(),
                    (String) row[2],
                    row[3] != null ? ((java.sql.Timestamp) row[3]).toLocalDateTime() : null,
                    row[4] != null ? ((java.sql.Timestamp) row[4]).toLocalDateTime() : null,
                    (String) row[5],
                    ((Number) row[6]).longValue(),
                    row[7] != null ? ((java.sql.Timestamp) row[7]).toLocalDateTime() : null
            ));
        }

        return result;
    }
}