package com.shoes.product.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing category
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryRequest {

    @Size(min = 2, max = 150, message = "Category name must be between 2 and 150 characters")
    private String name;

    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    @Size(min = 2, max = 200, message = "Category slug must be between 2 and 200 characters")
    private String slug;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String image;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private Boolean isActive;

    @Min(value = 0, message = "displayOrder must be 0 or greater")
    private Integer displayOrder;
}

