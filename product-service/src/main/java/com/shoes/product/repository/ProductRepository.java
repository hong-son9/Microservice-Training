package com.shoes.product.repository;

import com.shoes.product.entity.Product;
import com.shoes.product.entity.ProductStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Product entity
 * Includes EntityGraph definitions to prevent N+1 queries
 * Soft-deleted entities are automatically excluded via @Where annotation
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find product by SKU (with related data)
     */
    @EntityGraph(attributePaths = {"brand", "categories", "sizes"})
    Optional<Product> findBySku(String sku);

    /**
     * Find product by name (with related data)
     */
    @EntityGraph(attributePaths = {"brand", "categories"})
    Optional<Product> findByName(String name);

    /**
     * Find product by slug (with related data)
     */
    @EntityGraph(attributePaths = {"brand", "categories", "sizes"})
    Optional<Product> findBySlug(String slug);

    /**
     * Find product by ID with all related data
     */
    @EntityGraph(attributePaths = {"brand", "categories", "sizes", "images"})
    @Override
    Optional<Product> findById(Long id);

    /**
     * Find all products with eager loading
     */
    @EntityGraph(attributePaths = {"brand", "categories"})
    @Override
    List<Product> findAll();

    /**
     * Find products by IDs with eager loading
     */
    @EntityGraph(attributePaths = {"brand", "categories", "sizes"})
    List<Product> findByIdIn(List<Long> ids);

    /**
     * Find products by status
     */
    List<Product> findByStatusAndIsDeletedFalse(ProductStatus status);

    /**
     * Check if product with SKU exists
     */
    boolean existsBySku(String sku);

    /**
     * Check if product with name exists
     */
    boolean existsByName(String name);

    /**
     * Check if product with slug exists
     */
    boolean existsBySlug(String slug);
}
