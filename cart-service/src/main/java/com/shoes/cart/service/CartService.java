package com.shoes.cart.service;

import com.shoes.cart.dto.request.CartItemRequest;
import com.shoes.cart.dto.response.CartResponse;
import com.shoes.cart.entity.Cart;

import java.util.List;

public interface CartService {
    Cart getCartById(Long id);
    CartResponse getCartByUserId(Long userId);
    CartResponse addCart(CartItemRequest cartItemRequest);
    void removeProductsFromCart(Long userId, List<Long> productId);
}
