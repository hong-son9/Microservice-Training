package com.shoes.cart.client;

import com.shoes.cart.dto.ApiResponse;
import com.shoes.cart.dto.response.ProductResponse;
import com.shoes.cart.dto.response.ProductSizeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="product-service", path="/api/products")
public interface ProductClient {
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable("id") Long id);

    @GetMapping("/productSize")
    ResponseEntity<ApiResponse<ProductSizeResponse>> getProductSize(@RequestParam("sizeVn") Integer sizeVn, @RequestParam("productId") Long productId);
}
