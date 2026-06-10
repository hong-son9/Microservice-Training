package com.shoes.order.client;

import com.shoes.order.dto.ApiResponse;
import com.shoes.order.dto.response.ProductSizeResponse;
import com.shoes.order.dto.response.ProductSnapshotResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping("/api/products/snapshots")
    ApiResponse<List<ProductSnapshotResponse>> getProducts(@RequestParam("ids") List<Long> ids);

    @GetMapping("/api/products/productSize")
    ResponseEntity<ApiResponse<ProductSizeResponse>> getProductSize(@RequestParam("sizeVn") Integer sizeVn, @RequestParam("productId") Long productId);
}
