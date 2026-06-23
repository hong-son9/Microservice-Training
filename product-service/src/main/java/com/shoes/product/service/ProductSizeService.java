package com.shoes.product.service;

import com.shoes.product.dto.response.ProductSizeResponse;

public interface ProductSizeService {
    ProductSizeResponse getBySizeAndProductId(Integer sizeVn, Long productId);
}
