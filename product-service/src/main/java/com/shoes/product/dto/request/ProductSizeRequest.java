package com.shoes.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating/updating product size inventory
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSizeRequest {

    @NotNull(message = "Size (Vietnamese standard) is required")
    @Min(value = 1, message = "Size must be a valid Vietnamese shoe size")
    private Integer sizeVn;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be 0 or greater")
    private Integer quantity = 0;
}

