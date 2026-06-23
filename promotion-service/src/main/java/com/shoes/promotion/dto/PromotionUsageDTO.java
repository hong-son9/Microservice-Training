package com.shoes.promotion.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PromotionUsageDTO {
    private Long id;
    private Long promotionId;
    private String couponCode;
    private Long userId;
    private Long orderId;
    private Long discountApplied;
    private LocalDateTime usedAt;
}