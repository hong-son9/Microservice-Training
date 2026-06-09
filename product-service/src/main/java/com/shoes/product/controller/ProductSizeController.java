package com.shoes.product.controller;

import com.shoes.product.dto.ApiResponse;
import com.shoes.product.dto.Response.ProductSizeResponse;
import com.shoes.product.repository.ProductSizeRepository;
import com.shoes.product.service.ProductSizeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductSizeController {
    private final ProductSizeService productSizeService;
    @GetMapping("/productSize")
    public ResponseEntity<ApiResponse<ProductSizeResponse>> getBySizeAndProductId(@RequestParam("sizeVn") Integer sizeVn,
                                                                                 @RequestParam("productId") Long productId) {
        ProductSizeResponse productSizeResponse = productSizeService.getBySizeAndProductId(sizeVn, productId);
        return ResponseEntity.ok(
                ApiResponse.success(productSizeResponse, "Product size retrieved successfully")
        );
    }
}
