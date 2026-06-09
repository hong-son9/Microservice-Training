package com.shoes.product.mapper;

import com.shoes.product.dto.Request.ProductSizeRequest;
import com.shoes.product.dto.Response.ProductSizeResponse;
import com.shoes.product.entity.ProductSize;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * MapStruct mapper for ProductSize entity and DTOs
 */
@Mapper(componentModel = "spring")
public interface ProductSizeMapper {

    /**
     * Convert ProductSize entity to ProductSizeResponse DTO
     */
    @Named("toProductSizeResponse")
    ProductSizeResponse toResponse(ProductSize entity);

    /**
     * Convert ProductSizeRequest to ProductSize entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductSize toEntity(ProductSizeRequest request);
}

