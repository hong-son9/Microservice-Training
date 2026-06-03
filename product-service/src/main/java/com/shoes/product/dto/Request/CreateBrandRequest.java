package com.shoes.product.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBrandRequest {
    private String name;

    private String slug;

    private String logo;

    private String description;

    private Boolean isActive;
}
