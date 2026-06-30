/**
 * 
 */
package com.pms.floor.entity;

/**
 * 
 */
public class FloorDTO {
	
	private Long id;
    private String name;
    private Integer noOfRooms;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getNoOfRooms() {
		return noOfRooms;
	}
	public void setNoOfRooms(Integer noOfRooms) {
		this.noOfRooms = noOfRooms;
	}
    
}
