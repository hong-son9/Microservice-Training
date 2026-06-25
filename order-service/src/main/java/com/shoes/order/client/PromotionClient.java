package com.shoes.order.client;

import com.shoes.order.dto.ApiResponse;
import com.shoes.order.entity.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@FeignClient(name = "promotion-service")
public interface PromotionClient {

    @PostMapping("/api/promotions/apply-by-id")
    ResponseEntity<ApiResponse<PromotionUsageResponse>> applyCouponById(@RequestBody ApplyCouponIdRequest request);

    @GetMapping("/api/promotions/{id}")
    ResponseEntity<ApiResponse<PromotionResponse>> getPromotionBYId(@PathVariable("id") long id);


    @Data
    @AllArgsConstructor
    class ApplyCouponIdRequest {
        private Long promotionId;
        private Long userId;
        private Long orderId;
        private Long orderValue;
    }

    @Data
    class PromotionUsageResponse {
        private Long id;
        private Long discountApplied;
    }

    @Data
    class PromotionResponse {
        private Long id;
        private String couponCode;
        private String name;
        private String description;
        private DiscountType discountType;
        private Long discountValue;
        private Long maximumDiscountValue;
        private Long minOrderValue;
        private LocalDateTime startedAt;
        private LocalDateTime expiredAt;
        private Integer usageLimit;
        private Integer usedCount;
        private Integer usageLimitPerUser;
        private Boolean isActive;
        private Boolean isPublic;
    }
}