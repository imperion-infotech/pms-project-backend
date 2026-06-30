/**
 * 
 */
package com.pms.usersession.entity;

/**
 * 
 */
import java.time.LocalDate;
import java.time.LocalDateTime;


import com.pms.baseentity.BaseEntity;
import com.pms.hotel.entity.Hotel;
import com.pms.security.entity.User;
import com.pms.usersession.enums.SessionStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_session_log")
@Getter
@Setter
public class UserSessionLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false,insertable=false, updatable=false)
    private Hotel hotel;

    private LocalDate businessDate;

    private LocalDateTime loginTime;

    private LocalDateTime logoutTime;

    private String sessionId;

    @Enumerated(EnumType.STRING)
    private SessionStatus sessionStatus;

    private Long shiftDurationMinutes;
}