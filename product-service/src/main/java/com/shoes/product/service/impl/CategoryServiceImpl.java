package com.shoes.product.service.impl;

import com.shoes.product.dto.request.CreateCategoryRequest;
import com.shoes.product.dto.request.UpdateCategoryRequest;
import com.shoes.product.dto.response.CategoryResponse;
import com.shoes.product.entity.Category;
import com.shoes.product.exception.ConflictException;
import com.shoes.product.exception.ResourceNotFoundException;
import com.shoes.product.mapper.CategoryMapper;
import com.shoes.product.repository.CategoryRepository;
import com.shoes.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for Category management
 * Handles CRUD operations with proper validation and exception handling
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * Create a new category
     * Validates unique constraints (name, slug)
     */
    @Override
    public CategoryResponse create(CreateCategoryRequest request) {
        // Validate unique name
        if (categoryRepository.existsByName(request.getName())) {
            throw new ConflictException("Category", "name", request.getName());
        }

        // Validate unique slug
        if (categoryRepository.existsBySlug(request.getSlug())) {
            throw new ConflictException("Category", "slug", request.getSlug());
        }

        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    /**
     * Get category by ID
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return categoryMapper.toResponse(category);
    }

    /**
     * Get all categories (non-deleted)
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    /**
     * Get all active categories
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllActive() {
        List<Category> categories = categoryRepository.findByIsActiveTrue();
        return categories.stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    /**
     * Update existing category
     * Only updates non-null fields
     */
    @Override
    public CategoryResponse update(Long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        // Validate unique constraints if name/slug are being updated
        if (request.getName() != null && !request.getName().equals(category.getName())) {
            if (categoryRepository.existsByName(request.getName())) {
                throw new ConflictException("Category", "name", request.getName());
            }
        }

        if (request.getSlug() != null && !request.getSlug().equals(category.getSlug())) {
            if (categoryRepository.existsBySlug(request.getSlug())) {
                throw new ConflictException("Category", "slug", request.getSlug());
            }
        }

        categoryMapper.updateEntityFromRequest(request, category);
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(updatedCategory);
    }

    /**
     * Soft delete category (marks as deleted, preserves data)
     */
    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        category.softDelete();
        categoryRepository.save(category);
    }

    /**
     * Hard delete category (permanent deletion from database)
     * WARNING: Use only for data cleanup, not for production
     */
    @Override
    public void deleteHard(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        categoryRepository.delete(category);
    }
}
