/**
 * 
 */
package com.pms.hotel.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pms.baseentity.BaseEntity;
import com.pms.building.entity.Building;
import com.pms.floor.entity.Floor;
import com.pms.room.entity.RoomMaster;
import com.pms.roomtype.entity.RoomType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hotel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({
    "hibernateLazyInitializer",
    "handler"
})
public class Hotel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hotel_name", nullable = false)
    private String hotelName;

    private String url;

    private String address;

    private String city;

    private String state;

    private String country;

    private String zipCode;

    private String email;

    private String contactNumber;

    private String status;

    private String timezone;

    private String hotelLogo;

    private String hotelImage;

    /*
     * BUILDINGS
     */
    @OneToMany(mappedBy = "hotel")
    @JsonIgnore
    private Set<Building> buildings = new HashSet<>();

    /*
     * FLOORS
     */
    @OneToMany(mappedBy = "hotel")
    @JsonIgnore
    private Set<Floor> floors = new HashSet<>();

    /*
     * ROOMS
     */
    @OneToMany(mappedBy = "hotel")
    @JsonIgnore
    private Set<RoomMaster> rooms = new HashSet<>();

    /*
     * ROOM TYPES
     */
    @OneToMany(mappedBy = "hotel")
    @JsonIgnore
    private Set<RoomType> roomTypes = new HashSet<>();
}