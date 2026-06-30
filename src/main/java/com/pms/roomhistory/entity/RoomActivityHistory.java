/**
 * 
 */

package com.pms.roomhistory.entity;

import java.time.LocalDateTime;

import com.pms.baseentity.BaseEntity;
import com.pms.room.entity.RoomMaster;
import com.pms.stay.entity.StayDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "room_activity_history")
@Data
public class RoomActivityHistory extends BaseEntity {

	  private static final long serialVersionUID = 1L;

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    /*
	     * STAY DETAILS
	     */
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "stay_id")
	    private StayDetails stayDetails;

	    /*
	     * OLD ROOM
	     */
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "old_room_id")
	    private RoomMaster oldRoom;

	    /*
	     * NEW ROOM
	     */
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "new_room_id")
	    private RoomMaster newRoom;

	    /*
	     * SNAPSHOT VALUES
	     */
	    @Column(name = "old_room_name")
	    private String oldRoomName;

	    @Column(name = "new_room_name")
	    private String newRoomName;

	    /*
	     * HOTEL
	     */
	    @Column(name = "hotel_id")
	    private Long hotelId;

	    /*
	     * CHANGE REASON
	     */
	    @Column(name = "reason")
	    private String reason;

	    /*
	     * REMARKS
	     */
	    @Column(name = "remarks")
	    private String remarks;

	    /*
	     * AUDIT
	     */
	    @Column(name = "changed_by")
	    private Long changedBy;

	    @Column(name = "changed_by_name")
	    private String changedByName;

	    @Column(name = "changed_on")
	    private LocalDateTime changedOn;

	  
		
		 @Column(name = "guest_details_id")
	    private Long guestDetailsId;
		
		 @Column(name = "activity_type")
	    private String activityType;

	    @Column(name = "old_status")
	    private String oldStatus;

	    @Column(name = "new_status")
	    private String newStatus;

	    @Column(name = "activity_datetime")
	    private LocalDateTime activityDateTime;
	    
	    @Column(name = "room_master_id")
	    private Integer roomMasterId;
		
		  /*
	     * TRACEABILITY
	     */
	    @Column(name = "business_trace_id")
	    private String businessTraceId;

	    @Column(name = "request_trace_id")
	    private String requestTraceId;
}