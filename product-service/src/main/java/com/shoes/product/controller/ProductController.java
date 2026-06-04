package com.shoes.product.controller;

import com.shoes.product.dto.Request.CreateProductRequest;
import com.shoes.product.dto.Response.ProductResponse;
import com.shoes.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    ProductService productService;
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProductResponse create(@RequestBody CreateProductRequest request) {
        return productService.create(request);
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable("id") Long id) {
        return productService.getById(id);
    }

    @GetMapping()
    public List<ProductResponse> getAll() {
        return productService.getAll();
    }
}
