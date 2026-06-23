package com.shoes.product.service.impl;

import com.shoes.product.dto.request.CreateBrandRequest;
import com.shoes.product.dto.request.UpdateBrandRequest;
import com.shoes.product.dto.response.BrandResponse;
import com.shoes.product.entity.Brand;
import com.shoes.product.exception.ConflictException;
import com.shoes.product.exception.ResourceNotFoundException;
import com.shoes.product.mapper.BrandMapper;
import com.shoes.product.repository.BrandRepository;
import com.shoes.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for Brand management
 * Handles CRUD operations with proper validation and exception handling
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandMapper brandMapper;

    /**
     * Create a new brand
     * Validates unique constraints (name, slug)
     */
    @Override
    public BrandResponse create(CreateBrandRequest request) {
        // Validate unique name
        if (brandRepository.existsByName(request.getName())) {
            throw new ConflictException("Brand", "name", request.getName());
        }

        // Validate unique slug
        if (brandRepository.existsBySlug(request.getSlug())) {
            throw new ConflictException("Brand", "slug", request.getSlug());
        }

        Brand brand = brandMapper.toEntity(request);
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toResponse(savedBrand);
    }

    /**
     * Get brand by ID
     */
    @Override
    @Transactional(readOnly = true)
    public BrandResponse getById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));
        return brandMapper.toResponse(brand);
    }

    /**
     * Get all brands (non-deleted) - supports pagination
     */
    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> getAll() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream()
                .map(brandMapper::toResponse)
                .toList();
    }

    /**
     * Get all active brands only
     */
    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> getAllActive() {
        List<Brand> brands = brandRepository.findByIsActiveTrue();
        return brands.stream()
                .map(brandMapper::toResponse)
                .toList();
    }

    /**
     * Update existing brand
     * Only updates non-null fields
     */
    @Override
    public BrandResponse update(Long id, UpdateBrandRequest request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));

        // Validate unique constraints if name/slug are being updated
        if (request.getName() != null && !request.getName().equals(brand.getName())) {
            if (brandRepository.existsByName(request.getName())) {
                throw new ConflictException("Brand", "name", request.getName());
            }
        }

        if (request.getSlug() != null && !request.getSlug().equals(brand.getSlug())) {
            if (brandRepository.existsBySlug(request.getSlug())) {
                throw new ConflictException("Brand", "slug", request.getSlug());
            }
        }

        brandMapper.updateEntityFromRequest(request, brand);
        Brand updatedBrand = brandRepository.save(brand);
        return brandMapper.toResponse(updatedBrand);
    }

    /**
     * Soft delete brand (marks as deleted, preserves data)
     */
    @Override
    public void delete(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));
        brand.softDelete();
        brandRepository.save(brand);
    }

    /**
     * Hard delete brand (permanent deletion from database)
     * WARNING: Use only for data cleanup, not for production
     */
    @Override
    public void deleteHard(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));
        brandRepository.delete(brand);
    }
}
