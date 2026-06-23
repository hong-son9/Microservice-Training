package com.shoes.product.mapper;

import com.shoes.product.dto.request.CreateBrandRequest;
import com.shoes.product.dto.request.UpdateBrandRequest;
import com.shoes.product.dto.response.BrandResponse;
import com.shoes.product.entity.Brand;
import org.mapstruct.*;

/**
 * MapStruct mapper for Brand entity and DTOs
 * Handles conversion between entity, request, and response objects
 */
@Mapper(componentModel = "spring")
public interface BrandMapper {

    /**
     * Convert Brand entity to BrandResponse DTO
     */
    @Named("toBrandResponse")
    BrandResponse toResponse(Brand entity);

    /**
     * Convert CreateBrandRequest to Brand entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "deletedAt", ignore = true)
    Brand toEntity(CreateBrandRequest request);

    /**
     * Update Brand entity from UpdateBrandRequest
     * Only non-null fields are updated
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntityFromRequest(UpdateBrandRequest request, @MappingTarget Brand entity);
}

