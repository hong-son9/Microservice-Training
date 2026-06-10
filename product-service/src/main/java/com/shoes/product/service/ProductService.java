package com.shoes.product.service;

import com.shoes.product.dto.Request.CreateProductRequest;
import com.shoes.product.dto.Request.UpdateProductRequest;
import com.shoes.product.dto.Response.ProductResponse;
import com.shoes.product.dto.event.OrderCancelledEvent;
import com.shoes.product.dto.event.OrderPlacedEvent;

import java.util.List;

/**
 * Service interface for Product management
 * Defines CRUD operations for products with complex aggregate handling
 */
public interface ProductService {

    /**
     * Create a new product with sizes and categories
     */
    ProductResponse create(CreateProductRequest request);

    /**
     * Get product by ID with all related data
     */
    ProductResponse getById(Long id);

    /**
     * Get all products (non-deleted)
     */
    List<ProductResponse> getAll();

    /**
     * Get products by list of IDs (for snapshots)
     */
    List<ProductResponse> getAllById(List<Long> ids);

    /**
     * Get only active products
     */
    List<ProductResponse> getAllActive();

    /**
     * Update existing product
     */
    ProductResponse update(Long id, UpdateProductRequest request);

    /**
     * Soft delete - mark as deleted but preserve data
     */
    void delete(Long id);

    /**
     * Hard delete - permanent deletion from database
     */
    void deleteHard(Long id);

    void deductStock(List<OrderPlacedEvent.OrderItemEvent> itemEvents);
    void refundStock(List<OrderCancelledEvent.OrderItemCancelEvent> itemCancelEvents);
}
