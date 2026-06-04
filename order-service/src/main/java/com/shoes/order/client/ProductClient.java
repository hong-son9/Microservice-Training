package com.shoes.order.client;

import com.shoes.order.dto.Response.ProductSnapshotResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping("/api/products/snapshots")
    List<ProductSnapshotResponse> getProducts(@RequestParam("ids") List<Long> ids);
}
