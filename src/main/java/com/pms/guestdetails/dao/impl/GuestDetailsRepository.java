/**
 * 
 */
package com.pms.guestdetails.dao.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pms.common.repository.SoftDeleteRepository;
import com.pms.guestdetails.GuestDetails;
import com.pms.guestdetails.dto.RoomActivityResponseDTO;

/**
 * 
 */
public interface GuestDetailsRepository
		extends SoftDeleteRepository<GuestDetails, Long>, JpaSpecificationExecutor<GuestDetails>,GuestDetailsRepositoryCustom  {
	GuestDetails findByIdAndHotelIdAndIsDeletedFalse(Long id, Long hotelId);

	List<GuestDetails> findByHotelIdAndIsDeletedFalse(Long hotelId);

	List<GuestDetails> findByIsDeletedFalse();

	/*@Query("""
			    SELECT new com.pms.guestdetails.dto.RoomActivityResponseDTO(

			        gd.roomMasterId,
			        gd.id,
			        CONCAT(pd.firstName, ' ', pd.lastName),
			        gd.checkInDate,
			        gd.checkOutDate,
			        gd.guestDetailsStatus
			    )

			    FROM GuestDetails gd

			    LEFT JOIN PersonalDetails pd
			        ON pd.id = gd.personalDetailsId

			    WHERE (
			        gd.checkInDate BETWEEN :fromDate AND :toDate
			        OR
			        gd.checkOutDate BETWEEN :fromDate AND :toDate
			        OR
			        gd.createdOn BETWEEN :fromDate AND :toDate
			    )

			    ORDER BY gd.checkInDate ASC
			""")
	List<RoomActivityResponseDTO> getRoomActivities(LocalDateTime fromDate, LocalDateTime toDate);*/
	
	
	@Query(value = """
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
		            CONCAT(pd.first_name, ' ', pd.last_name) AS guest_name,
		            gd.check_in_date,
		            gd.check_out_date,
		            gd.guest_details_status,
		            pd.id AS personal_details_id,
		            gd.created_on
		        FROM guest_details gd
		        LEFT JOIN personal_details pd
		            ON pd.id = gd.personal_details_id
		        WHERE gd.created_on BETWEEN :fromDate AND :toDate
		        ORDER BY gd.room_master_id, gd.guest_details_id DESC
		    ) latest
		    ORDER BY latest.check_in_date ASC
		    """, nativeQuery = true)
		List<RoomActivityResponseDTO> getRoomActivities(
		        @Param("fromDate") LocalDateTime fromDate,
		        @Param("toDate") LocalDateTime toDate);

	List<GuestDetails> findByHotelIdAndIsDeletedFalseAndGuestDetailsStatus(Long hotelId, String guestCurrentStatus);

	@Query("""
			SELECT g
			FROM GuestDetails g
			WHERE g.hotelId = :hotelId
			AND LOWER(g.guestDetailsStatus) = LOWER('RESERVATION')
			AND g.checkInDate < CURRENT_TIMESTAMP
			AND g.isDeleted = false
			""")
			List<GuestDetails> findNoShowGuests(Long hotelId);

	@Query("""
			SELECT g
			FROM GuestDetails g
			WHERE g.hotelId = :hotelId
			AND g.isDeleted = false
			AND LOWER(g.guestDetailsStatus) = LOWER('CHECK_IN')
			AND DATE(g.checkOutDate) <= :businessDate
			""")
	List<GuestDetails> findDueOutGuests(Long hotelId, LocalDate businessDate);
	
	@Query("""
	        SELECT g
	        FROM GuestDetails g
	        WHERE g.hotelId = :hotelId
	          AND g.isDeleted = false
	          AND g.guestDetailsStatus = 'CHECK_OUT'
	    """)
	    List<GuestDetails> findCheckOutGuests(@Param("hotelId") Long hotelId);
	
	Long countByHotelIdAndGuestDetailsStatusAndIsDeletedFalse(
            Long hotelId,
            String guestDetailsStatus);
	
	@Query("""
			SELECT g
			FROM GuestDetails g
			WHERE g.hotelId = :hotelId
			AND LOWER(g.guestDetailsStatus) = LOWER('CHECK_IN')
			AND g.isDeleted = false
			""")
			List<GuestDetails> findCheckInGuests(Long hotelId);
	
	
	@Query(value = """
	        SELECT
	            g.guest_details_id,
	            p.first_name,
	            p.last_name,
	            d.document_number,
	            r.room_name,
	            g.check_in_date,
	            g.check_out_date,
	            rent.total_rental
	        FROM guest_details g
	        LEFT JOIN personal_details p
	            ON p.id = g.personal_details_id AND p.is_deleted = false
	        LEFT JOIN document_details d
	            ON d.id = g.document_details_id AND d.is_deleted = false
	        LEFT JOIN stay_details s
	            ON s.id = g.stay_details_id AND s.is_deleted = false
	        LEFT JOIN room_master r
	            ON r.id = s.room_master_id AND r.is_deleted = false
	        LEFT JOIN rent_details rent
	            ON rent.id = g.rent_details_id AND rent.is_deleted = false
	        WHERE g.is_deleted = false

	        AND (:firstName IS NULL
	                OR LOWER(p.first_name)
	                LIKE LOWER(CONCAT('%', CAST(:firstName AS TEXT), '%')))

	        AND (:lastName IS NULL
	                OR LOWER(p.last_name)
	                LIKE LOWER(CONCAT('%', CAST(:lastName AS TEXT), '%')))

	        AND (:documentNumber IS NULL
	                OR LOWER(d.document_number)
	                LIKE LOWER(CONCAT('%', CAST(:documentNumber AS TEXT), '%')))

	        AND (:roomName IS NULL
	                OR LOWER(r.room_name)
	                LIKE LOWER(CONCAT('%', CAST(:roomName AS TEXT), '%')))

	        AND (:checkInFromDate IS NULL
	                OR g.check_in_date >= CAST(:checkInFromDate AS TIMESTAMP))

	        AND (:checkInToDate IS NULL
	                OR g.check_in_date <= CAST(:checkInToDate AS TIMESTAMP))

	        AND (:checkOutFromDate IS NULL
	                OR g.check_out_date >= CAST(:checkOutFromDate AS TIMESTAMP))

	        AND (:checkOutToDate IS NULL
	                OR g.check_out_date <= CAST(:checkOutToDate AS TIMESTAMP))

	        AND (CAST(:totalRental AS NUMERIC) IS NULL
        OR rent.total_rental = CAST(:totalRental AS NUMERIC))

	    """, nativeQuery = true)
	List<Object[]> searchGuests(
	        @Param("firstName") String firstName,
	        @Param("lastName") String lastName,
	        @Param("documentNumber") String documentNumber,
	        @Param("roomName") String roomName,
	        @Param("checkInFromDate") LocalDateTime checkInFromDate,
	        @Param("checkInToDate") LocalDateTime checkInToDate,
	        @Param("checkOutFromDate") LocalDateTime checkOutFromDate,
	        @Param("checkOutToDate") LocalDateTime checkOutToDate,
	        @Param("totalRental") Double totalRental
	);
	
	
	
	
	
	/*
	@Query(value = """
	        SELECT
	            g.guest_details_id,
	            p.first_name,
	            p.last_name,
	            d.document_number,
	            r.room_name,
	            g.check_in_date,
	            g.check_out_date,
	            rent.total_rental
	        FROM multi_motels_pms_db.guest_details g
	        LEFT JOIN multi_motels_pms_db.personal_details p
	            ON p.id = g.personal_details_id AND p.is_deleted = false
	        LEFT JOIN multi_motels_pms_db.document_details d
	            ON d.id = g.document_details_id AND d.is_deleted = false
	        LEFT JOIN multi_motels_pms_db.stay_details s
	            ON s.id = g.stay_details_id AND s.is_deleted = false
	        LEFT JOIN multi_motels_pms_db.room_master r
	            ON r.id = s.room_master_id AND r.is_deleted = false
	        LEFT JOIN multi_motels_pms_db.rent_details rent
	            ON rent.id = g.rent_details_id AND rent.is_deleted = false
	        WHERE g.is_deleted = false

	        AND (:firstName IS NULL
	                OR LOWER(p.first_name)
	                LIKE LOWER(CONCAT('%', CAST(:firstName AS TEXT), '%')))

	        AND (:lastName IS NULL
	                OR LOWER(p.last_name)
	                LIKE LOWER(CONCAT('%', CAST(:lastName AS TEXT), '%')))

	        AND (:documentNumber IS NULL
	                OR LOWER(d.document_number)
	                LIKE LOWER(CONCAT('%', CAST(:documentNumber AS TEXT), '%')))

	        AND (:roomName IS NULL
	                OR LOWER(r.room_name)
	                LIKE LOWER(CONCAT('%', CAST(:roomName AS TEXT), '%')))

	        AND (:checkInFromDate IS NULL
	                OR g.check_in_date >= CAST(:checkInFromDate AS TIMESTAMP))

	        AND (:checkInToDate IS NULL
	                OR g.check_in_date <= CAST(:checkInToDate AS TIMESTAMP))

	        AND (:checkOutFromDate IS NULL
	                OR g.check_out_date >= CAST(:checkOutFromDate AS TIMESTAMP))

	        AND (:checkOutToDate IS NULL
	                OR g.check_out_date <= CAST(:checkOutToDate AS TIMESTAMP))

	        AND (CAST(:totalRental AS NUMERIC) IS NULL
        OR rent.total_rental = CAST(:totalRental AS NUMERIC))

	    """, nativeQuery = true)
	List<Object[]> searchGuests(
	        @Param("firstName") String firstName,
	        @Param("lastName") String lastName,
	        @Param("documentNumber") String documentNumber,
	        @Param("roomName") String roomName,
	        @Param("checkInFromDate") LocalDateTime checkInFromDate,
	        @Param("checkInToDate") LocalDateTime checkInToDate,
	        @Param("checkOutFromDate") LocalDateTime checkOutFromDate,
	        @Param("checkOutToDate") LocalDateTime checkOutToDate,
	        @Param("totalRental") Double totalRental
	);
	
	*/
	
	@Query("""
		    SELECT max(gd.id)
		    FROM GuestDetails gd
		    WHERE gd.roomMaster.id = :roomId and gd.hotelId=:hotelId and gd.isDeleted = false and gd.isActive=true
		    group by gd.roomMaster.id
		""")
		Long findOccupiedGuestDetailsIdByRoomId(@Param("roomId") Long roomId, Long hotelId);
	
	
/*	@Query("""
		    SELECT gd.id
		    FROM GuestDetails gd
		    WHERE gd.roomMaster.id = :roomId
		      //AND gd.guestDetailsStatus = 'CHECKED_IN'
		""")
		Optional<Long> findOccupiedGuestDetailsIdByRoomId(@Param("roomId") Long roomId);
	*/
	
	

}
