package com.pms.nightaudit.entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessDateRequest {

    private Long hotelId;

    private LocalDate businessDate;
}