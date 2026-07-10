/**
 * 
 */
package com.pms.guestprofile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.guestprofile.GuestProfileResponseDto;
import com.pms.guestprofile.service.IGuestProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/guest-profile")
@RequiredArgsConstructor
public class GuestProfileController {

    private final IGuestProfileService guestProfileService;

    @GetMapping("/{guestDetailsId}")
    public ResponseEntity<GuestProfileResponseDto> getProfile(
            @PathVariable Long guestDetailsId) {
        return ResponseEntity.ok(
                guestProfileService.getGuestProfile(guestDetailsId));
    }
}