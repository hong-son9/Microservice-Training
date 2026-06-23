package com.shoes.cart.controller;

import com.shoes.cart.config.SecurityUtils;
import com.shoes.cart.dto.ApiResponse;
import com.shoes.cart.dto.request.CartItemRequest;
import com.shoes.cart.dto.response.CartResponse;
import com.shoes.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
        return ResponseEntity.ok(ApiResponse.success(cartService.getCartByUserId(SecurityUtils.getCurrentUserId())));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @RequestBody CartItemRequest request) {

        CartResponse cart = cartService.addCart(request);
        return ResponseEntity.ok(ApiResponse.success(cart, "Product added to cart successfully"));
    }
}
