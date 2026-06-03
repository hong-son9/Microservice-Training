package com.shoes.product.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {
    private String name;
    private String slug;
    private String image = "";
    private String description;
    private Boolean isActive;
    private Integer displayOrder;
}
