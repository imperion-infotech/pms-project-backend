package com.pms.floor.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pms.baseentity.BaseEntity;
import com.pms.building.entity.Building;
import com.pms.hotel.entity.Hotel;
import com.pms.room.entity.RoomMaster;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
	    name = "floor",
	    uniqueConstraints = {
	        @UniqueConstraint(
	            columnNames = {
	                "hotel_id",
	                "name"
	            }
	        )
	    }
	)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
@JsonIgnoreProperties(
	    {"hibernateLazyInitializer", "handler"}
	)
public class Floor extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "floor_id")
    private Long id;

    @Column(name = "floor_name")
    private String name;

    private String description;

    private Integer noOfRooms;

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
    private Hotel hotel;

    /*
     * BUILDING
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;
    

    /*
     * ROOMS
     */
    @OneToMany(mappedBy = "floor")
    @JsonIgnore
    private Set<RoomMaster> rooms = new HashSet<>();
}