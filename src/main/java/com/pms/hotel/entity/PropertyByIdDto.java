/**
 * 
 */
package com.pms.hotel.entity;

/**
 * 
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyByIdDto {

    private Long id;
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
   /* private String hotelLogo;
    private String hotelImage;*/

    private String ownerUsername;
    private String ownerEmail;
}