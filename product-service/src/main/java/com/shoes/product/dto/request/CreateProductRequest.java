package com.shoes.product.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DTO for creating a new product
 * Properly separated from entity with comprehensive validation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    @NotBlank(message = "Product SKU is required")
    @Size(min = 1, max = 50, message = "SKU must be between 1 and 50 characters")
    private String sku;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 300, message = "Product name must be between 2 and 300 characters")
    private String name;

    @NotBlank(message = "Product slug is required")
    @Size(min = 2, max = 350, message = "Product slug must be between 2 and 350 characters")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    private String slug;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private Long price;

    @DecimalMin(value = "0.00", message = "Sale price must be 0 or greater")
    private Long salePrice;

    @DecimalMin(value = "0.00", message = "Import price must be 0 or greater")
    private Long importPrice;

    @NotBlank(message = "Brand name is required")
    private String brandName;

    @NotEmpty(message = "At least one product size is required")
    @Valid
    private List<ProductSizeRequest> sizes = new ArrayList<>();

    @NotEmpty(message = "At least one category is required")
    private Set<String> category = new HashSet<>();
}

