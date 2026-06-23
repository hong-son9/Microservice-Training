package com.shoes.cart.dto.request;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private Integer sizeVn;
}