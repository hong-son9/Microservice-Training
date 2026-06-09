package com.shoes.cart.dto.Request;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private Integer sizeVn;
}