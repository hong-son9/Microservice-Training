package com.shoes.promotion.dto;

import com.shoes.promotion.entity.DiscountType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PromotionDTO {
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