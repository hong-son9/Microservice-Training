package com.shoes.product.dto.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {
    private String name;
    private String slug;
    private String image = "";
    private String description;
    private Boolean isActive;
    private Integer displayOrder;
}
