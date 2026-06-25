package com.shoes.promotion.controller;

import com.shoes.promotion.dto.ApiResponse;
import com.shoes.promotion.dto.ApplyCouponIdRequest;
import com.shoes.promotion.dto.PromotionUsageDTO;
import com.shoes.promotion.dto.PromotionUsageResponse;
import com.shoes.promotion.service.impl.PromotionUsageService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionUsageController {

    private final PromotionUsageService usageService;

    // Endpoint gọi khi khách hàng ấn áp dụng coupon ở trang thanh toán hoặc submit đơn hàng
    @PostMapping("/apply")
    public ResponseEntity<PromotionUsageDTO> applyCoupon(@RequestBody ApplyCouponRequest request) {
        PromotionUsageDTO result = usageService.applyPromotion(
                request.getCouponCode(),
                request.getUserId(),
                request.getOrderId(),
                request.getOrderValue()
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/rollback/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> rollbackCoupon(@PathVariable("orderId") Long orderId) {
        usageService.rollbackPromotion(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PromotionUsageDTO>> getUserHistory(@PathVariable("userId") Long userId, Pageable pageable) {
        return ResponseEntity.ok(usageService.getHistoryByUserId(userId, pageable));
    }

    @PostMapping("/apply-by-id")
    public ResponseEntity<ApiResponse<PromotionUsageResponse>> applyCouponById(
            @RequestBody ApplyCouponIdRequest request) {

        PromotionUsageResponse response = usageService.applyPromotionById(
                request.getPromotionId(),
                request.getUserId(),
                request.getOrderId(),
                request.getOrderValue()
        );

        return ResponseEntity.ok(ApiResponse.success(response, "Promotion usage successfully applied"));
    }

    @Data
    public static class ApplyCouponRequest {
        private String couponCode;
        private Long userId;
        private Long orderId;
        private Long orderValue;
    }
}