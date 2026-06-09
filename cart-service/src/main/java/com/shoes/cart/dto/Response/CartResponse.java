package com.shoes.cart.dto.Response;

import lombok.Data;

import java.util.List;

@Data
public class CartResponse {
    private Long id;
    private Long userId;
    private List<CartItemResponse> items;
    private long subtotal;
    private int totalQuantity;
}
