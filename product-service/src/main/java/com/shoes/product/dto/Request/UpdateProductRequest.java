package com.shoes.product.dto.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * DTO for updating an existing product
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    @Size(min = 1, max = 50, message = "SKU must be between 1 and 50 characters")
    private String sku;

    @Size(min = 2, max = 300, message = "Product name must be between 2 and 300 characters")
    private String name;

    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    @Size(min = 2, max = 350, message = "Product slug must be between 2 and 350 characters")
    private String slug;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private Long price;

    @DecimalMin(value = "0.00", message = "Sale price must be 0 or greater")
    private Long salePrice;

    @DecimalMin(value = "0.00", message = "Import price must be 0 or greater")
    private Long importPrice;

    private String brandName;

    @Valid
    private List<ProductSizeRequest> sizes;

    private Set<String> category;

    // For admin use - update product status
    private String status;
}

