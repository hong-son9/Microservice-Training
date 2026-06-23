package com.shoes.product.service.impl;

import com.shoes.product.dto.response.ProductSizeResponse;
import com.shoes.product.entity.ProductSize;
import com.shoes.product.repository.ProductSizeRepository;
import com.shoes.product.service.ProductSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSizeServiceImpl implements ProductSizeService {
    private final ProductSizeRepository productSizeRepository;

    @Override
    public ProductSizeResponse getBySizeAndProductId(Integer sizeVn, Long productId) {
        ProductSize productSize = productSizeRepository.findByProductIdAndSizeVn(productId, sizeVn)
                .orElseThrow(() -> new RuntimeException("Product size not found"));
        return ProductSizeResponse.builder()
                .id(productSize.getId())
                .quantity(productSize.getQuantity())
                .sizeVn(productSize.getSizeVn()).build();
    }
}
