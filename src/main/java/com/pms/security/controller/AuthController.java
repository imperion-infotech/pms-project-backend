/**
 * 
 */
/**
 * 
 */
package com.pms.security.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.hotel.entity.Hotel;
import com.pms.hotel.repository.HotelRepository;
import com.pms.hotel.repository.UserHotelMappingRepository;
import com.pms.nightaudit.entity.BusinessDate;
import com.pms.nightaudit.repository.BusinessDateRepository;
import com.pms.security.dto.AuthResponse;
import com.pms.security.dto.HotelDTO;
import com.pms.security.dto.HotelSelectionRequest;
import com.pms.security.dto.LoginRequest;
import com.pms.security.dto.LoginResponse;
import com.pms.security.dto.RefreshRequest;
import com.pms.security.dto.RegisterRequest;
import com.pms.security.entity.RefreshToken;
import com.pms.security.entity.User;
import com.pms.security.entity.UserHotelMapping;
import com.pms.security.repository.UserRepository;
import com.pms.security.service.IUserService;
import com.pms.security.service.PasswordService;
import com.pms.security.service.RefreshTokenService;
import com.pms.security.service.TokenBlacklistService;
import com.pms.security.util.JwtUtil;
import com.pms.usersession.entity.UserSessionLog;
import com.pms.usersession.enums.SessionStatus;
import com.pms.usersession.repository.UserSessionLogRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/auth")
public class AuthController {

	static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final IUserService userService;
	private final TokenBlacklistService blacklistService;
	private final UserRepository userRepository;
	@Autowired
	private RefreshTokenService refreshTokenService;
	@Autowired
	private PasswordService passwordService;
	@Autowired
	private UserHotelMappingRepository mappingRepository;

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private UserSessionLogRepository sessionLogRepository;

	@Autowired
	private BusinessDateRepository businessDateRepository;

	public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, IUserService userService,
			TokenBlacklistService blacklistService, UserRepository userRepository) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.userService = userService;
		this.blacklistService = blacklistService;
		this.userRepository = userRepository;
	}

	@PostMapping("/login")
	@Transactional
	public LoginResponse login(@RequestBody LoginRequest request, HttpSession httpSession) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.username, request.password));

		User user = userRepository.findByUsernameWithRolesAndIsDeletedFalse(request.username)
				.orElseThrow(() -> new RuntimeException("User not found"));

		httpSession.setAttribute("user", user);

		List<UserHotelMapping> mappings = mappingRepository.findByUserId(user.getId());

		List<HotelDTO> hotels = mappings.stream()
				.filter(m -> m.getHotel() != null && Boolean.FALSE.equals(m.getHotel().getIsDeleted())
						&& Boolean.TRUE.equals(m.getHotel().getIsActive()))
				.map(m -> new HotelDTO(m.getHotel().getId(), m.getHotel().getHotelName())).toList();

		String token = jwtUtil.generateToken(user, null);

		RefreshToken refreshToken = refreshTokenService.createToken(user);

		LoginResponse response = new LoginResponse();

		response.setToken(token);
		response.setHotels(hotels);
		response.setRefreshToken(refreshToken.getToken());

		return response;
	}

	//=========================
	/*
	@PostMapping("/select-hotel")
	public String selectHotel(@RequestParam Long hotelId,
			@RequestParam String username) {

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));

		boolean valid = mappingRepository
				.existsByUserIdAndHotelId(user.getId(), hotelId);

		if (!valid) {
			throw new ResourceNotFoundException("Unauthorized hotel access");
		}

		UserHotelMapping mapping = mappingRepository.findByUserId(user.getId())
				.stream()
				.filter(m -> m.getHotel().getId().equals(hotelId))
				.findFirst()
				.get();

		// 🔥 token WITH hotelId
		return jwtUtil.generateToken(user, hotelId);
	}
	
	*/
	
	
	
	
	
	//===================
	@PostMapping("/select-hotel")
	@Transactional
	public String selectHotel(@RequestParam Long hotelId,
			@RequestParam String username, HttpSession session) {

		User user = (User) session.getAttribute("user");

		if (user == null) {
			return ("User session expired");
		}

		
		Hotel hotel = hotelRepository.findByIdAndIsDeletedFalse(hotelId)
				.orElseThrow(() -> new RuntimeException("Hotel not found"));

		session.setAttribute("hotelId", hotel.getId());

		session.setAttribute("hotelName", hotel.getHotelName());

		
		BusinessDate businessDate =
		        businessDateRepository
		        .findByHotelIdAndCurrentBusinessDateTrue(hotel.getId())
		        .orElseThrow(() ->
		                new RuntimeException("Business Date not found"));

		UserSessionLog log = new UserSessionLog();

		log.setUser(user);

		log.setHotel(hotel);
		
		log.setHotelId(hotel.getId());

		log.setBusinessDate(businessDate.getBusinessDate());

		log.setLoginTime(LocalDateTime.now());

		log.setSessionId(session.getId());

		log.setSessionStatus(SessionStatus.ACTIVE);
		
		log.setCreatedBy(user.getId());
		
		log.setUpdatedBy(user.getId());

		sessionLogRepository.save(log);

//		return ResponseEntity.ok("Hotel selected successfully");
		return jwtUtil.generateToken(user, hotelId);
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
		try {
			User savedUser = userService.registerNewUser(request);
			return ResponseEntity.ok("User registered successfully with ID: " + savedUser.getId());
		} catch (RuntimeException e) {
			logger.error("Exception in controller of registerUser api :" + e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/logout")
	@Transactional
	public ResponseEntity<?> logout(HttpSession session) {

		User user = (User) session.getAttribute("user");

		if (user != null) {

			Optional<UserSessionLog> activeSession = sessionLogRepository
					.findTopByUserIdAndSessionStatusAndIsDeletedFalseOrderByLoginTimeDesc(user.getId(),
							SessionStatus.ACTIVE);

			if (activeSession.isPresent()) {

				UserSessionLog log = activeSession.get();

				log.setLogoutTime(LocalDateTime.now());

				log.setSessionStatus(SessionStatus.LOGGED_OUT);

				if (log.getLoginTime() != null) {

					long minutes = java.time.Duration.between(log.getLoginTime(), log.getLogoutTime()).toMinutes();

					log.setShiftDurationMinutes(minutes);
				}

				sessionLogRepository.save(log);
			}
		}

		session.invalidate();

		return ResponseEntity.ok("Logout successful");
	}

	// 🔄 REFRESH TOKEN API
	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {

		RefreshToken refreshToken = refreshTokenService.validateToken(request.refreshToken);
		Long hotelId = 1L;

		String newAccessToken = jwtUtil.generateToken(refreshToken.getUser(), hotelId);

		return ResponseEntity.ok(new AuthResponse(newAccessToken, request.refreshToken));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestParam String email) {
		passwordService.createPasswordResetToken(email);
		return ResponseEntity.ok("Password reset link sent to your email.");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
		passwordService.resetPassword(token, newPassword);
		return ResponseEntity.ok("Password updated successfully.");
	}
	
}
