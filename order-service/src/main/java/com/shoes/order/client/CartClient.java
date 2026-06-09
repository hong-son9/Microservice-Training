package com.shoes.order.client;

import com.shoes.order.dto.ApiResponse;
import com.shoes.order.dto.Response.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cart-service", path = "/api/carts")
public interface CartClient {

    @GetMapping
    ResponseEntity<ApiResponse<CartResponse>> getCartByUserId();
}
