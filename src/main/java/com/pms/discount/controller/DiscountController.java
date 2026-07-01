/**
 * 
 */
package com.pms.discount.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pms.discount.dto.DiscountDTO;
import com.pms.discount.service.IDiscountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final IDiscountService service;

//    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'DISCOUNT_CREATE')")		
    @PostMapping("/user/creatediscount")
    public DiscountDTO createDiscount(@RequestBody DiscountDTO dto) {
        return service.createDiscount(dto);
    }

//    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'DISCOUNT_UPDATE')")
    @PutMapping("/user/updatediscount/{id}")
    public DiscountDTO updateDiscount(@PathVariable Long id,
                              @RequestBody DiscountDTO dto) {
        return service.updateDiscount(id, dto);
    }

//    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'DISCOUNT_VIEW')")
    @GetMapping("/user/getdiscount/{id}")
    public DiscountDTO getByDiscountId(@PathVariable Long id) {
        return service.getByDiscountId(id);
    }

//    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'DISCOUNT_VIEW')")
    @GetMapping("/user/getalldiscount")
    public List<DiscountDTO> getAllDiscount() {
        return service.getAllDiscount();
    }

//    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'DISCOUNT_DELETE')")
    @DeleteMapping("/user/deletediscount/{id}")
    public Boolean deleteDiscount(@PathVariable Long id) {
        return service.deleteDiscount(id);
    }
    
//    @PreAuthorize("@permissionChecker.hasPermission(authentication, 'DISCOUNT_VIEW')")
    @GetMapping("/user/guest/{guestId}")
    public ResponseEntity<List<DiscountDTO>> getDiscountsByGuestId(
            @PathVariable Long guestId) {

        return ResponseEntity.ok(service.getDiscountsByGuestId(guestId));
    }
}