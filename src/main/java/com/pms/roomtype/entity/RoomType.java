/**
 * 
 */
package com.pms.roomtype.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pms.baseentity.BaseEntity;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "room_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
@JsonIgnoreProperties({
    "hibernateLazyInitializer",
    "handler"
})
public class RoomType extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    private Long id;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "room_type_name")
    private String roomTypeName;

    private Double price;

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
     * ROOMS
     */
    @OneToMany(mappedBy = "roomType")
    @JsonIgnore
    private Set<RoomMaster> rooms = new HashSet<>();
}