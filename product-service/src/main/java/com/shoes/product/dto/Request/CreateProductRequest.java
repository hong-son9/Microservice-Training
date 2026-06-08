package com.shoes.product.dto.Request;

import com.shoes.product.entity.ProductSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    private String sku;
    private String name;
    private String slug;
    private String description;

    private Long price;
    private Long salePrice;
    private Long importPrice;

    private String brandName;
    private List<ProductSize> sizes = new ArrayList<>();
    private Set<String> category;
}