package com.pms.usersession.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.usersession.entity.UserSessionLog;
import com.pms.usersession.enums.SessionStatus;

public interface UserSessionLogRepository
        extends JpaRepository<UserSessionLog, Long> {

    Optional<UserSessionLog>
    findTopByUserIdAndSessionStatusAndIsDeletedFalseOrderByLoginTimeDesc(
            Long userId,
            SessionStatus sessionStatus);
    
    List<UserSessionLog> findBySessionStatusAndIsDeletedFalse(SessionStatus sessionStatus);
}