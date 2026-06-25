package com.shoes.promotion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PromotionValidateResultDTO {
    private boolean isValid;
    private String reasonCode;
    private String message;
    private Long discountAmount;
}