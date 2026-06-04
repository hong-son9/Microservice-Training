package com.shoes.order.entity;

/**
 * Trang thai don hang — luong cua he thong:
 *
 *   PENDING   → admin xac nhan → CONFIRMED
 *   CONFIRMED → admin gan shipper → SHIPPING
 *   SHIPPING  → giao thanh cong → DELIVERED
 *   SHIPPING  → giao that bai → RETURNED
 *   PENDING/CONFIRMED → user/admin huy → CANCELLED
 *   DELIVERED → khach tra hang trong 7 ngay → REFUNDED
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPING,
    DELIVERED,
    CANCELLED,
    RETURNED,
    REFUNDED
}
