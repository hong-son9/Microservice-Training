package com.shoes.product.service.Impl;

import com.shoes.product.dto.Request.CreateBrandRequest;
import com.shoes.product.dto.Request.CreateCategoryRequest;
import com.shoes.product.dto.Response.BrandResponse;
import com.shoes.product.dto.Response.CategoryResponse;
import com.shoes.product.entity.Brand;
import com.shoes.product.entity.Category;
import com.shoes.product.repository.BrandRepository;
import com.shoes.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    BrandRepository brandRepository;
    @Override
    public BrandResponse create(CreateBrandRequest request) {
        if (brandRepository.existsByName(request.getName())) {
            throw new RuntimeException("Brand name already exists");
        }
        Brand brand =  Brand.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .logo(request.getLogo())
                .isActive(request.getIsActive())
                .build();
        return toBrandResponse(brandRepository.save(brand));
    }

    @Override
    public BrandResponse getById(Long id) {
        return null;
    }

    @Override
    public List<BrandResponse> getAll() {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }
    private BrandResponse toBrandResponse(Brand brand) {
        return BrandResponse.builder()
                .name(brand.getName())
                .slug(brand.getSlug())
                .description(brand.getDescription())
                .logo(brand.getLogo())
                .isActive(brand.getIsActive())
                .build();
    }
}
