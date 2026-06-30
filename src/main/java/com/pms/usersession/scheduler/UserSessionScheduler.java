/**
 * 
 */
package com.pms.usersession.scheduler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pms.usersession.entity.UserSessionLog;
import com.pms.usersession.enums.SessionStatus;
import com.pms.usersession.repository.UserSessionLogRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Component
@RequiredArgsConstructor
public class UserSessionScheduler {

    private final UserSessionLogRepository sessionRepository;
    
    @Value("${session.expire.hours}")
    private Long sessionExpireHours;

    @Scheduled(cron = "0 */30 * * * *")
    @Transactional
    public void expireInactiveSessions() {

        List<UserSessionLog> activeSessions =
                sessionRepository.findBySessionStatusAndIsDeletedFalse(
                        SessionStatus.ACTIVE);

        LocalDateTime now = LocalDateTime.now();

        for (UserSessionLog session : activeSessions) {

            // Example: auto-expire after 8 hours
            if (session.getLoginTime().plusHours(sessionExpireHours).isBefore(now)) {

                session.setLogoutTime(now);

                session.setSessionStatus(
                        SessionStatus.SESSION_EXPIRED);

                session.setShiftDurationMinutes(
                        Duration.between(
                                session.getLoginTime(),
                                now)
                                .toMinutes());

                sessionRepository.save(session);
            }
        }
    }
}