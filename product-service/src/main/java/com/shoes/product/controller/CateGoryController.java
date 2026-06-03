package com.shoes.product.controller;

import com.shoes.product.dto.Response.CategoryResponse;
import com.shoes.product.dto.Request.CreateCategoryRequest;
import com.shoes.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/categories")
public class CateGoryController {
    @Autowired
    CategoryService categoryService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public CategoryResponse create(@RequestBody CreateCategoryRequest categoryRequest) {
        return categoryService.create(categoryRequest);
    };
}
