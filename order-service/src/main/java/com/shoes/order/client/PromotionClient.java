package com.shoes.order.client;

import com.shoes.order.dto.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "promotion-service")
public interface PromotionClient {

    @PostMapping("/api/promotions/apply-by-id")
    ResponseEntity<ApiResponse<PromotionUsageResponse>> applyCouponById(@RequestBody ApplyCouponIdRequest request);

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
}