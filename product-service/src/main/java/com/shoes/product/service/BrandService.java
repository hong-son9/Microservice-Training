package com.shoes.product.service;

import com.shoes.product.dto.Request.CreateBrandRequest;
import com.shoes.product.dto.Request.UpdateBrandRequest;
import com.shoes.product.dto.Response.BrandResponse;

import java.util.List;

/**
 * Service interface for Brand management
 * Defines CRUD operations for brands
 */
public interface BrandService {

    /**
     * Create a new brand
     */
    BrandResponse create(CreateBrandRequest request);

    /**
     * Get brand by ID
     */
    BrandResponse getById(Long id);

    /**
     * Get all brands (non-deleted)
     */
    List<BrandResponse> getAll();

    /**
     * Get only active brands
     */
    List<BrandResponse> getAllActive();

    /**
     * Update existing brand
     */
    BrandResponse update(Long id, UpdateBrandRequest request);

    /**
     * Soft delete - mark as deleted but preserve data
     */
    void delete(Long id);

    /**
     * Hard delete - permanent deletion from database
     */
    void deleteHard(Long id);
}
