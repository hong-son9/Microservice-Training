package com.shoes.product.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBrandRequest {

    @NotBlank(message = "Brand name is required")
    @Size(min = 2, max = 150, message = "Brand name must be between 2 and 150 characters")
    private String name;

    @NotBlank(message = "Brand slug is required")
    @Size(min = 2, max = 200, message = "Brand slug must be between 2 and 200 characters")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    private String slug;

    @Size(max = 500, message = "Logo URL must not exceed 500 characters")
    private String logo;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "isActive status is required")
    private Boolean isActive = true;
}


