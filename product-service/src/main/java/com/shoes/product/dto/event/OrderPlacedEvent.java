package com.shoes.product.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent {
    private Long orderId;
    private String orderCode;
    private Long buyerUserId;
    private Long promotionId;
    private List<OrderItemEvent> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemEvent {
        private Long productId;
        private Integer quantity;
        private Integer sizeVn;
    }
}