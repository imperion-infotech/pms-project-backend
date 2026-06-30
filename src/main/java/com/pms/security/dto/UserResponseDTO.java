package com.pms.security.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.pms.security.entity.Role;
import com.pms.security.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor   // ✅ ADD THIS
@NoArgsConstructor    // ✅ optional but recommended
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
    private Boolean enabled;
    private List<HotelDTO> hotels;
    private String userPermissionArray;
    private String password;
//    private Long hotelId;
    
    private UserResponseDTO convertToDTO(User user) {

        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .password(user.getPassword())
                .roles(
                    user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
                )

                .hotels(
                    user.getMappings().stream()
                        .map(mapping -> {
                            HotelDTO dto = new HotelDTO();
                            dto.setId(mapping.getHotel().getId());
                            dto.setName(mapping.getHotel().getHotelName());
                            return dto;
                        })
                        .collect(Collectors.toList())
                )

                .build();
    }

}