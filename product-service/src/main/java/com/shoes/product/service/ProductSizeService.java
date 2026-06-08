package com.shoes.product.service;

import com.shoes.product.dto.Response.ProductResponse;
import com.shoes.product.dto.Response.ProductSizeResponse;

public interface ProductSizeService {
    ProductSizeResponse getBySizeAndProductId(Integer sizeVn, Long productId);
}
