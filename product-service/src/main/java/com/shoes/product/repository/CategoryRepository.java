package com.shoes.product.repository;

import com.shoes.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Category entity
 * Extends JpaRepository with additional custom queries
 * Soft-deleted entities are automatically excluded via @Where annotation
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find category by name
     */
    Optional<Category> findByName(String name);

    /**
     * Check if category with name exists
     */
    boolean existsByName(String name);

    /**
     * Check if category with slug exists
     */
    boolean existsBySlug(String slug);

    /**
     * Find all active categories
     */
    List<Category> findByIsActiveTrue();

    /**
     * Find category by slug
     */
    Optional<Category> findBySlug(String slug);
}
