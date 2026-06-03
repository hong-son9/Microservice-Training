package com.shoes.promotion.entity;

/**
 * Cach tinh giam gia.
 * PERCENTAGE — giam % gia (vd 10% = 0.1), can them maximumDiscountValue de gioi han
 * FIXED      — giam mot so tien co dinh / don vi san pham (vd 50.000d/doi)
 * SHIPPING   — mien phi van chuyen (future use)
 */
public enum DiscountType {
    PERCENTAGE,
    FIXED,
    SHIPPING
}
