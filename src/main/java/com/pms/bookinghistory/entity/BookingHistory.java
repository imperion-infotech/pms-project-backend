/**
 * 
 */
package com.pms.bookinghistory.entity;

import java.time.LocalDateTime;

import com.pms.baseentity.BaseEntity;
import com.pms.bookinghistory.enums.BookingHistoryAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "booking_history")
@Getter
@Setter
public class BookingHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(name = "hotel_id", nullable = false)
    private Long hotelId;

    @Column(name = "guest_details_id")
    private Long guestDetailsId;

    @Column(name = "rent_details_id")
    private Long rentDetailsId;

    @Column(name = "room_master_id")
    private Long roomMasterId;

    @Column(name = "room_type_id")
    private Long roomTypeId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "guest_name")
    private String guestName;

    @Column(name = "check_in")
    private LocalDateTime checkIn;

    @Column(name = "check_out")
    private LocalDateTime checkOut;

    @Column(name = "source")
    private String source;

    @Column(name = "old_status")
    private String oldStatus;

    @Column(name = "new_status")
    private String newStatus;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "remarks")
    private String remarks;
    
    private String bookingRefNo;
    private Double balance;
    private Double paymentAmount;
    

}