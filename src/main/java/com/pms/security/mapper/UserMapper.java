package com.pms.security.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.pms.security.dto.HotelDTO;
import com.pms.security.dto.UserRequestDTO;
import com.pms.security.dto.UserResponseDTO;
import com.pms.security.entity.Role;
import com.pms.security.entity.User;
import com.pms.security.entity.UserHotelMapping;

public class UserMapper {

    // 🔹 Entity → ResponseDTO
    public static UserResponseDTO toResponseDTO(User user) {

        if (user == null) return null;

        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.getEnabled())

                // ✅ Roles
                .roles(user.getRoles() != null
                        ? user.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toSet())
                        : Set.of())

                // ✅ Hotels from mapping
                .hotels(user.getMappings() != null
                        ? user.getMappings().stream()
                            .map(UserHotelMapping::getHotel)
                            .map(hotel -> {
                                HotelDTO dto = new HotelDTO();
                                dto.setId(hotel.getId());
                                dto.setName(hotel.getHotelName());
                                return dto;
                            })
                            .collect(Collectors.toList())
                        : List.of())

                .build();
    }
}