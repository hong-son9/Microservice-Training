package com.shoes.product.mapper;

import com.shoes.product.dto.Request.CreateProductRequest;
import com.shoes.product.dto.Request.ProductSizeRequest;
import com.shoes.product.dto.Request.UpdateProductRequest;
import com.shoes.product.dto.Response.ProductResponse;
import com.shoes.product.dto.Response.ProductSizeResponse;
import com.shoes.product.entity.Category;
import com.shoes.product.entity.Product;
import com.shoes.product.entity.ProductSize;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * MapStruct mapper for Product entity and DTOs
 * Handles complex nested object mappings (sizes, categories, images)
 */
@Mapper(componentModel = "spring", uses = {BrandMapper.class, CategoryMapper.class})
public interface ProductMapper {

    /**
     * Convert Product entity to ProductResponse DTO
     */
    @Named("toProductResponse")
    @Mapping(target = "brandName", source = "brand.name")
    @Mapping(target = "category", source = "categories", qualifiedByName = "categoriesToNames")
    @Mapping(target = "sizes", source = "sizes", qualifiedByName = "sizesToResponses")
    ProductResponse toResponse(Product entity);

    /**
     * Convert CreateProductRequest to Product entity
     * Note: Sizes and categories are handled separately in service layer
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "sizes", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "viewCount", constant = "0L")
    @Mapping(target = "totalSold", constant = "0L")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "deletedAt", ignore = true)
    Product toEntity(CreateProductRequest request);

    /**
     * Update Product entity from UpdateProductRequest
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "sizes", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "totalSold", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntityFromRequest(UpdateProductRequest request, @MappingTarget Product entity);

    /**
     * Convert ProductSize entity to ProductSizeResponse DTO
     */
    @Named("toProductSizeResponse")
    ProductSizeResponse toProductSizeResponse(ProductSize entity);

    /**
     * Convert ProductSizeRequest to ProductSize entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductSize toProductSize(ProductSizeRequest request);

    /**
     * Helper method: Convert Set<Category> to Set<String> (category names)
     */
    @Named("categoriesToNames")
    default Set<String> categoriesToNames(Set<Category> categories) {
        if (categories == null) {
            return Set.of();
        }
        return categories.stream()
                .map(Category::getName)
                .collect(Collectors.toSet());
    }

    /**
     * Helper method: Convert List<ProductSize> to List<ProductSizeResponse>
     */
    @Named("sizesToResponses")
    default java.util.List<ProductSizeResponse> sizesToResponses(java.util.List<ProductSize> sizes) {
        if (sizes == null) {
            return java.util.List.of();
        }
        return sizes.stream()
                .map(this::toProductSizeResponse)
                .collect(Collectors.toList());
    }
}

