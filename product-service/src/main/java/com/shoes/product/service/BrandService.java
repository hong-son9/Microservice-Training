package com.shoes.product.service;

import com.shoes.product.dto.Request.CreateBrandRequest;
import com.shoes.product.dto.Response.BrandResponse;
import com.shoes.product.dto.Response.CategoryResponse;
import com.shoes.product.dto.Request.CreateCategoryRequest;

import java.util.List;

public interface BrandService {
    BrandResponse create(CreateBrandRequest request);

    BrandResponse getById(Long id);

    List<BrandResponse> getAll();

//    UserResponse update(Long id, UpdateUserRequest request);

    void delete(Long id);
}
