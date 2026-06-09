package com.shoes.product.mapper;

import com.shoes.product.dto.Request.CreateCategoryRequest;
import com.shoes.product.dto.Request.UpdateCategoryRequest;
import com.shoes.product.dto.Response.CategoryResponse;
import com.shoes.product.entity.Category;
import org.mapstruct.*;

/**
 * MapStruct mapper for Category entity and DTOs
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * Convert Category entity to CategoryResponse DTO
     */
    @Named("toCategoryResponse")
    CategoryResponse toResponse(Category entity);

    /**
     * Convert CreateCategoryRequest to Category entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "deletedAt", ignore = true)
    Category toEntity(CreateCategoryRequest request);

    /**
     * Update Category entity from UpdateCategoryRequest
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntityFromRequest(UpdateCategoryRequest request, @MappingTarget Category entity);
}

