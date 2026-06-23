package com.shoes.product.service;

import com.shoes.product.dto.request.CreateCategoryRequest;
import com.shoes.product.dto.request.UpdateCategoryRequest;
import com.shoes.product.dto.response.CategoryResponse;

import java.util.List;

/**
 * Service interface for Category management
 * Defines CRUD operations for categories
 */
public interface CategoryService {

    /**
     * Create a new category
     */
    CategoryResponse create(CreateCategoryRequest request);

    /**
     * Get category by ID
     */
    CategoryResponse getById(Long id);

    /**
     * Get all categories (non-deleted)
     */
    List<CategoryResponse> getAll();

    /**
     * Get only active categories
     */
    List<CategoryResponse> getAllActive();

    /**
     * Update existing category
     */
    CategoryResponse update(Long id, UpdateCategoryRequest request);

    /**
     * Soft delete - mark as deleted but preserve data
     */
    void delete(Long id);

    /**
     * Hard delete - permanent deletion from database
     */
    void deleteHard(Long id);
}
