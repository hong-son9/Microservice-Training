package com.shoes.cart.service;

import com.shoes.cart.dto.Request.CartItemRequest;
import com.shoes.cart.dto.Response.CartResponse;
import com.shoes.cart.entity.Cart;

import java.util.List;

public interface CartService {
    Cart getCartById(Long id);
    CartResponse getCartByUserId();
    CartResponse addCart(CartItemRequest cartItemRequest);
    void removeProductsFromCart(List<Long> productId);
}
