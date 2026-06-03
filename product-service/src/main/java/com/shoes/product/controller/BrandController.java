package com.shoes.product.controller;

import com.shoes.product.dto.Request.CreateBrandRequest;
import com.shoes.product.dto.Response.BrandResponse;
import com.shoes.product.entity.Brand;
import com.shoes.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/brands")
public class BrandController {
    @Autowired
    BrandService brandService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public BrandResponse create(@RequestBody CreateBrandRequest createBrandRequest) {
        return brandService.create(createBrandRequest);
    }
}
