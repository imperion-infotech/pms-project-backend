/**
 * 
 */
package com.pms.usersession.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.usersession.entity.UserSessionLog;
import com.pms.usersession.enums.SessionStatus;
import com.pms.usersession.repository.UserSessionLogRepository;
import com.pms.usersession.service.IUserSessionLogService;

import jakarta.transaction.Transactional;

/**
 * 
 */
@Service
public class UserSessionLogServiceImpl  implements IUserSessionLogService {
	
	
	@Autowired
	UserSessionLogRepository userSessionLogRepository;
	
	@Transactional
	public void markSessionExpired(Long userId) {

	    Optional<UserSessionLog> activeSession =
	            userSessionLogRepository
	                    .findTopByUserIdAndSessionStatusAndIsDeletedFalseOrderByLoginTimeDesc(
	                            userId,
	                            SessionStatus.ACTIVE);

	    if (activeSession.isPresent()) {

	        UserSessionLog log = activeSession.get();

	        log.setLogoutTime(LocalDateTime.now());

	        log.setSessionStatus(SessionStatus.SESSION_EXPIRED);

	        log.setShiftDurationMinutes(
	                Duration.between(
	                        log.getLoginTime(),
	                        log.getLogoutTime())
	                        .toMinutes());

	        userSessionLogRepository.save(log);
	    }
	}

}
