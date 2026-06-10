package com.shoes.order.client;

import com.shoes.order.config.FeignClientConfig;
import com.shoes.order.dto.ApiResponse;
import com.shoes.order.dto.response.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cart-service", path = "/api/cart", configuration = FeignClientConfig.class)
public interface CartClient {

    @GetMapping
    ResponseEntity<ApiResponse<CartResponse>> getCartByUserId();
}
