package com.shoes.order.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelledEvent {
    private Long orderId;
    private String orderCode;
    private Long buyerUserId;
    private List<OrderItemCancelEvent> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemCancelEvent {
        private Long productId;
        private Integer quantity;
        private Integer sizeVn;
    }
}
