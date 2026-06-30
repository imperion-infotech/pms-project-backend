/**
 * 
 */
package com.pms.room.entity;

import java.io.Serializable;

import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pms.baseentity.BaseEntity;
import com.pms.floor.entity.Floor;
import com.pms.hotel.entity.Hotel;
import com.pms.roomstatus.entity.RoomStatus;
import com.pms.roomtype.entity.RoomType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
	    name = "room_master",
	    uniqueConstraints = {
	        @UniqueConstraint(
	            columnNames = {
	                "hotel_id",
	                "room_name"
	            }
	        )
	    }
	)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SQLRestriction("is_deleted = false")
public class RoomMaster extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_master_seq")
    @SequenceGenerator(
            name = "room_master_seq",
            sequenceName = "room_master_seq",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;
    
    @Column(name = "building_id")
    private Long buildingId;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "room_short_name")
    private String roomShortName;

    private Boolean smoking;

    private Boolean handicap;
    
    private Boolean pet;

    @Column(name = "non_room")
    private Boolean nonRoom;

    @Column(name = "non_smoking")
    private Boolean nonSmoking;

    
    
    /*
     * HOTEL
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "hotel_id",
        referencedColumnName = "id",
        insertable = false,
        updatable = false
    )
    @JsonIgnore
    @JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
    })
    private Hotel hotel;

    /*
     * FLOOR
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    @JsonIgnoreProperties(
    	    {"hibernateLazyInitializer", "handler"}
    	)
//    @JsonIgnore
    private Floor floor;

    /*
     * ROOM TYPE
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id")
    @JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
    })
    private RoomType roomType;

    /*
     * ROOM STATUS
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_status_id")
    @JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
    })
    private RoomStatus roomStatus;
    
}