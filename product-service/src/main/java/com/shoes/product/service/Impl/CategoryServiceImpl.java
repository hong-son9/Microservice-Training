package com.shoes.product.service.Impl;

import com.shoes.product.dto.Response.CategoryResponse;
import com.shoes.product.dto.Request.CreateCategoryRequest;
import com.shoes.product.entity.Category;
import com.shoes.product.repository.CategoryRepository;
import com.shoes.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public CategoryResponse create(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category name already exists");
        }
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .slug(request.getSlug())
                .image(request.getImage())
                .isActive(request.getIsActive())
                .displayOrder(request.getDisplayOrder())
                .build();
        return toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse getById(Long id) {
        return null;
    }

    @Override
    public List<CategoryResponse> getAll() {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }
    private CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .description(category.getDescription())
                .displayOrder(category.getDisplayOrder())
                .image(category.getImage())
                .isActive(category.getIsActive())
                .name(category.getName())
                .slug(category.getSlug()).build();
    }
}
