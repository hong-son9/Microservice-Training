package com.shoes.product.service;

import com.shoes.product.dto.Request.CreateCategoryRequest;
import com.shoes.product.dto.Request.CreateProductRequest;
import com.shoes.product.dto.Response.CategoryResponse;
import com.shoes.product.dto.Response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse create(CreateProductRequest request);

    ProductResponse getById(Long id);

    List<ProductResponse> getAll();

//    UserResponse update(Long id, UpdateUserRequest request);

    void delete(Long id);
}
