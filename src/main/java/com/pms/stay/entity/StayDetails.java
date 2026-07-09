/**
 * 
 */
package com.pms.stay.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pms.baseentity.BaseEntity;
import com.pms.building.entity.Building;
import com.pms.floor.entity.Floor;
import com.pms.room.entity.RoomMaster;
import com.pms.roomtype.entity.RoomType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="stay_details")
@SQLRestriction("is_deleted = false")
public class StayDetails extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="floor_id")
    private Long floorId;
    
    @Column(name="building_id")
    private Long buildingId;
    
    @Column(name="room_type_id")
    private Long roomTypeId;
    
    @Column(name="room_master_id")
    private Long roomMasterId;
    
    @Column(name="comment",nullable = true)
    private String comment;
    
    @NotNull(message = "RATE type is required")
	@Enumerated(EnumType.STRING) // Store enum name as text in DB
    @Column(nullable = true)
    private RateTypeEnum rateTypeEnum;
    
//    @NotNull(message = "STAY status is required") 	
   	@Enumerated(EnumType.STRING)
   	@Column(name = "stay_status_enum")
   	private StayStatusEnum stayStatusEnum = StayStatusEnum.UnConfirmed;
   	
//    @Column(nullable = false)
    private Integer noOfGuest;
    
 // ✅ NEW FIELDS (Soft Delete)
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    private LocalDateTime deletedOn;

    private Long deletedBy;
    
    @Column(name = "request_trace_id")
    private String requestTraceId;

    @Column(name = "business_trace_id")
    private String businessTraceId;
    
    @Column(name = "personal_details_id")
    private Long personalDetailsId;
    
    @Column(name = "taxExempt")
    private Boolean taxExempt = false;
    
	public Long getPersonalDetailsId() {
		return personalDetailsId;
	}

	public void setPersonalDetailsId(Long personalDetailsId) {
		this.personalDetailsId = personalDetailsId;
	}

	public String getRequestTraceId() {
		return requestTraceId;
	}

	public void setRequestTraceId(String requestTraceId) {
		this.requestTraceId = requestTraceId;
	}

	public String getBusinessTraceId() {
		return businessTraceId;
	}

	public void setBusinessTraceId(String businessTraceId) {
		this.businessTraceId = businessTraceId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
    @JoinColumn(name = "floor_id", insertable = false, updatable = false)
	@JsonIgnore
    private Floor floor;
	
	public Long getFloorId() {
		return floorId;
	}

	public void setFloorId(Long floorId) {
		this.floorId = floorId;
	}

	@ManyToOne
    @JoinColumn(name = "building_id", insertable = false, updatable = false)
	@JsonIgnore
    private Building building;
	
	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

	@ManyToOne
    @JoinColumn(name = "room_type_id", insertable = false, updatable = false)
	@JsonIgnore
    private RoomType roomType;
	
	public Long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(Long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	@ManyToOne
    @JoinColumn(name = "room_master_id", insertable = false, updatable = false)
	@JsonIgnore
    private RoomMaster roomMaster;
	
	public Floor getFloor() {
		return floor;
	}

	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public RoomMaster getRoomMaster() {
		return roomMaster;
	}

	public void setRoomMaster(RoomMaster roomMaster) {
		this.roomMaster = roomMaster;
	}

	public Long getRoomMasterId() {
		return roomMasterId;
	}

	public void setRoomMasterId(Long roomMasterId) {
		this.roomMasterId = roomMasterId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public RateTypeEnum getRateTypeEnum() {
		return rateTypeEnum;
	}

	public void setRateTypeEnum(RateTypeEnum rateTypeEnum) {
		this.rateTypeEnum = rateTypeEnum;
	}

	public StayStatusEnum getStayStatusEnum() {
		return stayStatusEnum;
	}

	public void setStayStatusEnum(StayStatusEnum stayStatusEnum) {
		this.stayStatusEnum = stayStatusEnum;
	}

	public Integer getNoOfGuest() {
		return noOfGuest;
	}

	public void setNoOfGuest(Integer noOfGuest) {
		this.noOfGuest = noOfGuest;
	}
	
	public LocalDateTime getDeletedOn() {
		return deletedOn;
	}

	public void setDeletedOn(LocalDateTime deletedOn) {
		this.deletedOn = deletedOn;
	}

	public void setDeletedBy(Long deletedBy) {
		this.deletedBy = deletedBy;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	

	public Long getDeletedBy() {
		return deletedBy;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StayDetails [id=");
		builder.append(id);
		builder.append(", floorId=");
		builder.append(floorId);
		builder.append(", buildingId=");
		builder.append(buildingId);
		builder.append(", roomTypeId=");
		builder.append(roomTypeId);
		builder.append(", roomMasterId=");
		builder.append(roomMasterId);
		builder.append(", comment=");
		builder.append(comment);
		builder.append(", rateTypeEnum=");
		builder.append(rateTypeEnum);
		builder.append(", stayStatusEnum=");
		builder.append(stayStatusEnum);
		builder.append(", noOfGuest=");
		builder.append(noOfGuest);
		builder.append(", floor=");
		builder.append(floor);
		builder.append(", building=");
		builder.append(building);
		builder.append(", roomType=");
		builder.append(roomType);
		builder.append(", roomMaster=");
		builder.append(roomMaster);
		builder.append("]");
		return builder.toString();
	}

}
