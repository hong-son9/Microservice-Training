package com.shoes.cart.dto.event;

import lombok.Data;

import java.util.List;

@Data
public class OrderPlacedEvent {
    private Long orderId;
    private String orderCode;
    private Long buyerUserId;
    private List<Long> selectedProductIds;

}