package com.shoes.product.dto.Response;

import com.shoes.product.entity.ProductSize;
import com.shoes.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class ProductResponse {

    private Long id;

    private String sku;
    private String name;
    private String slug;
    private String description;

    private Long price;
    private Long salePrice;
    private Long importPrice;

    private Long viewCount;
    private Long totalSold;

    private String brandName;
    private List<ProductSizeResponse> sizes = new ArrayList<>();
    private Set<String> category;

    private ProductStatus status;
}
