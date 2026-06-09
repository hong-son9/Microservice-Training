package com.shoes.cart.client;

import com.shoes.cart.dto.ApiResponse;
import com.shoes.cart.dto.Response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="product-service", path="/api/products")
public interface ProductClient {
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable("id") Long id);
}
