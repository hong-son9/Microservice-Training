package com.shoes.promotion.dto;

import lombok.Data;

@Data
public class ApplyCouponIdRequest {
    private Long promotionId;
    private Long userId;
    private Long orderId;
    private Long orderValue;
}