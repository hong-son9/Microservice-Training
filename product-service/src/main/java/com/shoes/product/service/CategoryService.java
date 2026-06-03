package com.shoes.product.service;

import com.shoes.product.dto.Response.CategoryResponse;
import com.shoes.product.dto.Request.CreateCategoryRequest;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CreateCategoryRequest request);

    CategoryResponse getById(Long id);

    List<CategoryResponse> getAll();

//    UserResponse update(Long id, UpdateUserRequest request);

    void delete(Long id);
}
