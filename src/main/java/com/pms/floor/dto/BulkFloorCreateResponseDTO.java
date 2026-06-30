/**
 * 
 */
package com.pms.floor.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
public class BulkFloorCreateResponseDTO {

    private Integer totalCreated;

    private List<String> createdFloors;

    private List<String> duplicateFloors;
}