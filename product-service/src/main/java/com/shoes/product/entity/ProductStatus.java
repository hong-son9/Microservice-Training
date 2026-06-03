package com.shoes.product.entity;

/**
 * Trang thai san pham.
 * DRAFT — admin tao nhung chua public
 * ACTIVE — dang ban (hien thi tren shop)
 * INACTIVE — tam ngung ban (an khoi shop nhung khong xoa)
 * OUT_OF_STOCK — het hang toan bo size
 */
public enum ProductStatus {
    DRAFT,
    ACTIVE,
    INACTIVE,
    OUT_OF_STOCK
}
