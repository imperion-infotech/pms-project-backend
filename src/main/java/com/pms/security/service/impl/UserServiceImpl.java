/**
 * 
 */
package com.pms.security.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pms.exception.ResourceNotFoundException;
import com.pms.hotel.entity.Hotel;
import com.pms.hotel.repository.HotelRepository;
import com.pms.search.specification.UserSpecification;
import com.pms.security.configuration.HotelContext;
import com.pms.security.configuration.UserContext;
import com.pms.security.dto.HotelDTO;
import com.pms.security.dto.RegisterRequest;
import com.pms.security.dto.UserRequestDTO;
import com.pms.security.dto.UserResponseDTO;
import com.pms.security.entity.Role;
import com.pms.security.entity.User;
import com.pms.security.entity.UserHotelMapping;
import com.pms.security.repository.RoleRepository;
import com.pms.security.repository.UserRepository;
import com.pms.security.service.AuthService;
import com.pms.security.service.BaseHotelService;
import com.pms.security.service.IUserService;
import com.pms.security.util.SecurityUtils;

/**
 * 
 */
@Service
public class UserServiceImpl extends BaseHotelService implements IUserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final HotelRepository hotelRepository;
	private final PasswordEncoder passwordEncoder;

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private AuthService authService;

	@Autowired
	private SecurityUtils securityUtils;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
			RoleRepository roleRepository, HotelRepository hotelRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.hotelRepository = hotelRepository;
	}

	public User registerNewUser(RegisterRequest request) {

		User user = new User();
		user.setUsername(request.getUsername());
//		user.setPassword(request.getPassword());
//		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		String encoded = passwordEncoder.encode(request.getPassword());
		
	        
	       if(request.getRole().equals("ROLE_SUPER_ADMIN") || request.getRole().equals("ROLE_HOTEL_OWNER"))
	       {
	    	   user.setPassword(bcryptPasswordEncoder.encode(request.getPassword()));
	       } else
	       {
	    	   user.setPassword(request.getPassword());
	       }
		
		
		
		user.setEnabled(true);
		user.setEmail(request.getEmailId());

		// Role
		Role role = roleRepository.findByName(request.getRole())
				.orElseThrow(() -> new RuntimeException("Role not found"));
		user.setRoles(Set.of(role));

		// Hotel Mapping
		Hotel hotel = hotelRepository.findById(request.getHotelId())
				.orElseThrow(() -> new RuntimeException("Hotel not found"));

		UserHotelMapping mapping = new UserHotelMapping();
		mapping.setUser(user); // 🔥 IMPORTANT
		mapping.setHotel(hotel); // 🔥 IMPORTANT

		user.getMappings().add(mapping); // 🔥 VERY IMPORTANT

		return userRepository.save(user); // cascade will save mapping
	}

	public UserResponseDTO createUser(UserRequestDTO dto) {

		// Check if username already exists
		if (userRepository.findByUsernameAndIsDeletedFalse(dto.getUsername()).isPresent()) {
			throw new ResourceNotFoundException("Username already exists");
		}

		User user = new User();
		user.setUsername(dto.getUsername());
		user.setEmail(dto.getEmail());
//		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		 Set<Role> rolesTest =  user.getRoles();
	        
	        boolean isSuperAdmin = rolesTest.stream()
	                .anyMatch(role -> "ROLE_SUPER_ADMIN".equals(role.getName()));
	        
	        boolean isHotelOwner = rolesTest.stream()
	                .anyMatch(role -> "ROLE_HOTEL_OWNER".equals(role.getName()));
	        
	       if(isSuperAdmin || isHotelOwner)
	       {
	    	   user.setPassword(bcryptPasswordEncoder.encode(dto.getPassword()));
	       } else
	       {
	    	   user.setPassword(dto.getPassword());
	       }
		
//		user.setPassword(dto.getPassword());
		user.setEnabled(true);
		user.setUserPermissionArray(dto.getUserPermissionArray());
		

		// ✅ Roles
		if (dto.getRoleIds() == null || dto.getRoleIds().isEmpty()) {
			throw new RuntimeException("At least one role is required");
		}

		Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));

		if (roles.isEmpty()) {
			throw new RuntimeException("Invalid roles provided");
		}

		user.setRoles(roles);

		System.out.println("Roles found: " + roles.size());

		// ✅ Hotel Mappings
		Set<UserHotelMapping> mappings = new HashSet<>();

		for (Long hotelId : dto.getHotelIds()) {

			Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new RuntimeException("Hotel not found"));

			UserHotelMapping mapping = new UserHotelMapping();
			mapping.setUser(user);
			mapping.setHotel(hotel);

			// optional: set role if exists in DTO
			// mapping.setRole("ADMIN");

			mappings.add(mapping);
			user.setHotelId(hotelId);
		}

		user.setMappings(mappings);

		// ✅ Save
		User savedUser = userRepository.save(user);

		logger.info("Roles size: " + user.getRoles().size());
		user.getRoles().forEach(r -> System.out.println(r.getName()));

		// ✅ RETURN DTO (IMPORTANT)
		return convertToDTO(savedUser);
	}

	// 🔹 GET USER BY ID
	public UserResponseDTO getUser(Long id) {
		User user = userRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new RuntimeException("User not found"));

		return convertToDTO(user);
	}

	// 🔹 GET ALL USERS
	public List<UserResponseDTO> getAllUsers() {
		Long hotelId = HotelContext.getHotelId();
		List<User> users= null;

		if (hotelId == null) {
			throw new ResourceNotFoundException("Hotel not selected");
		}

		if( isSuperAdmin() == true)
		{
			users = userRepository.findByIsDeletedFalseAndHotelId(hotelId);
		} else
		{
		    users = userRepository.findByIsDeletedFalseAndHotelId(hotelId);
		}

		return users.stream().map(this::convertToDTO).toList();

	}

	private UserResponseDTO convertToDTO(User user) {
		UserResponseDTO dto = new UserResponseDTO();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setEnabled(user.getEnabled());
		dto.setUserPermissionArray(user.getUserPermissionArray());
        dto.setPassword(user.getPassword());
		
		dto.setHotels(user.getMappings().stream().map(mapping -> {
			Hotel hotel = mapping.getHotel();
			HotelDTO hotelDTO = new HotelDTO();
			hotelDTO.setId(hotel.getId());
			hotelDTO.setName(hotel.getHotelName());
			return hotelDTO;
		}).collect(Collectors.toList()));

		// Roles
		dto.setRoles(user.getRoles() != null ? user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
				: new HashSet<>());

		// ⚠️ IMPORTANT: Do NOT include full mappings or hotel object
		// Only minimal data if needed

		return dto;
	}

	public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

	    User user = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    if (userRepository.existsByUsernameAndIdNot(dto.getUsername(), id)) {
	        throw new ResourceNotFoundException("Username already exists");
	    }

	    user.setUsername(dto.getUsername());
	    user.setEmail(dto.getEmail());
	    user.setUserPermissionArray(dto.getUserPermissionArray());
	    
	    Set<Role> rolesTest =  user.getRoles();
        
        boolean isSuperAdmin = rolesTest.stream()
                .anyMatch(role -> "ROLE_SUPER_ADMIN".equals(role.getName()));
        
        boolean isHotelOwner = rolesTest.stream()
                .anyMatch(role -> "ROLE_HOTEL_OWNER".equals(role.getName()));
        
       if(isSuperAdmin || isHotelOwner)
       {
    	   user.setPassword(bcryptPasswordEncoder.encode(dto.getPassword()));
       } else
       {
    	   user.setPassword(dto.getPassword());
       }
	
	    // Update roles
	    if (dto.getRoleIds() != null) {
	        Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));
	        user.setRoles(roles);
	    }
	    
	    

	    // Update hotels
	    if (dto.getHotelIds() != null) {
	        user.getMappings().clear();

	        dto.getHotelIds().forEach(hotelId -> {

	            Hotel hotel = hotelRepository.findById(hotelId)
	                    .orElseThrow(() -> new RuntimeException("Hotel not found"));

	            UserHotelMapping mapping = new UserHotelMapping();
	            mapping.setHotel(hotel);

	            user.addMapping(mapping);
	            user.setHotelId(hotelId);
	        });
	    }

	    User savedUser = userRepository.save(user);

	    return convertToDTO(savedUser);
	}
	// 🔹 DELETE USER
	public void deleteUser(Long id) {
		User user = getUserEntity(id);

		Long userId = UserContext.getUserId();
		if (userId == null) {
			throw new ResourceNotFoundException("User not selected");
		}

		boolean isSuperAdmin = user.getRoles()
		        .stream()
		        .anyMatch(role ->
		                role.getName()
		                    .equals("ROLE_SUPER_ADMIN"));
		
		if(!isSuperAdmin) {
			user.setIsDeleted(true); // make sure field name is correct
			user.setDeletedOn(LocalDateTime.now());
			user.setDeletedBy(userId);
			user.setEnabled(false);
	
			userRepository.save(user);
		} else {
			
			throw new ResourceNotFoundException("Superadmin cannot be deleted");
		}
	
	}

	private User getUserEntity(Long id) {
		return userRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	public List<UserResponseDTO> search(String username, Boolean enabled) {

		Long hotelId = HotelContext.getHotelId();

		if (!isSuperAdmin()) {
			validateHotelAccess(hotelId);
		}

		Specification<User> spec = Specification.where(UserSpecification.hasHotelId(hotelId))
				.and(UserSpecification.isNotDeleted()).and(UserSpecification.hasUsername(username))
				.and(UserSpecification.hasEnabled(enabled));

		// ✅ RETURN DTO (NOT ENTITY)
		return userRepository.findAll(spec).stream().map(this::convertToDTO).toList();
	}

}
