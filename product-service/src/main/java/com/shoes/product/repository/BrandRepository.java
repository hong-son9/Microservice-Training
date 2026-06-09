package com.shoes.product.repository;

import com.shoes.product.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Brand entity
 * Extends JpaRepository with additional custom queries
 * Soft-deleted entities are automatically excluded via @Where annotation
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    /**
     * Find brand by name
     */
    Optional<Brand> findByName(String name);

    /**
     * Check if brand with name exists
     */
    boolean existsByName(String name);

    /**
     * Check if brand with slug exists
     */
    boolean existsBySlug(String slug);

    /**
     * Find all active brands
     */
    List<Brand> findByIsActiveTrue();

    /**
     * Find brand by slug
     */
    Optional<Brand> findBySlug(String slug);
}