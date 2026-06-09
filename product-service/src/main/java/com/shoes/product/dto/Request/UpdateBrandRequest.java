package com.shoes.product.dto.Request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing brand
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBrandRequest {

    @Size(min = 2, max = 150, message = "Brand name must be between 2 and 150 characters")
    private String name;

    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    @Size(min = 2, max = 200, message = "Brand slug must be between 2 and 200 characters")
    private String slug;

    @Size(max = 500, message = "Logo URL must not exceed 500 characters")
    private String logo;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private Boolean isActive;
}

