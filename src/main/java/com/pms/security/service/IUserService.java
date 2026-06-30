/**
 * 
 */
package com.pms.security.service;

import java.util.List;

import com.pms.hotel.entity.Hotel;
import com.pms.security.dto.RegisterRequest;
import com.pms.security.dto.UserRequestDTO;
import com.pms.security.dto.UserResponseDTO;
import com.pms.security.entity.User;

/**
 * 
 */
public interface IUserService {
	
	public User registerNewUser(RegisterRequest request);
	public UserResponseDTO createUser(UserRequestDTO dto);
	public UserResponseDTO getUser(Long id);
	public List<UserResponseDTO> getAllUsers();
	public UserResponseDTO updateUser(Long id, UserRequestDTO dto);
	public void deleteUser(Long id);
	public List<UserResponseDTO> search(String username, Boolean enabled);
	

}
