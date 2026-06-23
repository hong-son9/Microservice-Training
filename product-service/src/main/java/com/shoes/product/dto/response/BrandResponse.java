package com.shoes.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandResponse {
    private String name;

    private String slug;

    private String logo;

    private String description;

    private Boolean isActive;
}
