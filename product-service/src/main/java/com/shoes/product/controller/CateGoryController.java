package com.shoes.product.controller;

import com.shoes.product.dto.ApiResponse;
import com.shoes.product.dto.response.CategoryResponse;
import com.shoes.product.dto.request.CreateCategoryRequest;
import com.shoes.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@RequestBody CreateCategoryRequest categoryRequest) {
        CategoryResponse response = categoryService.create(categoryRequest);
        return new ResponseEntity<>(
                ApiResponse.created(response, "Category created successfully"),
                HttpStatus.CREATED
        );
    };
}
