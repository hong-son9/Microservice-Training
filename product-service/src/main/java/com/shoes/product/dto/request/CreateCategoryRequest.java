package com.shoes.product.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 150, message = "Category name must be between 2 and 150 characters")
    private String name;

    @NotBlank(message = "Category slug is required")
    @Size(min = 2, max = 200, message = "Category slug must be between 2 and 200 characters")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    private String slug;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String image;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "isActive status is required")
    private Boolean isActive = true;

    @NotNull(message = "displayOrder is required")
    @Min(value = 0, message = "displayOrder must be 0 or greater")
    private Integer displayOrder = 0;
}


