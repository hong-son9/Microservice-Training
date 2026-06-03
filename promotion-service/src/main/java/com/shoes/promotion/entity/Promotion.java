package com.shoes.promotion.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Promotion / coupon entity.
 *
 * Public vs Private:
 *   - isPublic=true  → ai cung dung duoc (tu dong apply khi du dieu kien)
 *   - isPublic=false → can nhap code thu cong (vd voucher cho khach VIP)
 *
 * Quan ly thoi han + so lan dung de tranh abuse.
 */
@Entity
@Table(name = "promotions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_promotion_code", columnNames = "coupon_code")
        },
        indexes = {
                @Index(name = "idx_promo_active", columnList = "is_active, is_public, expired_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
public class Promotion extends BaseEntity {

    /** Ma coupon — VD: "SALE50", "FREESHIP" — khach nhap o trang checkout */
    @Column(name = "coupon_code", nullable = false, length = 50)
    private String couponCode;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    private DiscountType discountType;

    /**
     * Voi PERCENTAGE: gia tri tu 1-100 (vd 10 = giam 10%)
     * Voi FIXED:      so VND giam per don vi (vd 50000 = giam 50k/doi)
     */
    @Column(name = "discount_value", nullable = false)
    private Long discountValue;

    /**
     * Muc giam toi da khi discount_type = PERCENTAGE.
     * VD: 10% nhung khong qua 100.000d/don → maximum_discount_value = 100000
     * Voi FIXED type — bo qua hoac dat bang discount_value.
     */
    @Column(name = "maximum_discount_value")
    private Long maximumDiscountValue;

    /** Don hang toi thieu de apply (VND) — null nghia la khong yeu cau */
    @Column(name = "min_order_value")
    private Long minOrderValue;

    /** Thoi gian bat dau hieu luc */
    @Column(name = "started_at")
    private LocalDateTime startedAt;

    /** Het han */
    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    /** Gioi han tong so lan dung toan he thong (null = unlimited) */
    @Column(name = "usage_limit")
    private Integer usageLimit;

    /** Da dung bao nhieu lan — tang khi co don thanh cong */
    @Column(name = "used_count", nullable = false)
    @Builder.Default
    private Integer usedCount = 0;

    /** Moi user dung toi da bao nhieu lan (null = unlimited) */
    @Column(name = "usage_limit_per_user")
    private Integer usageLimitPerUser;

    /** true = admin bat coupon, false = tat */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /** true = auto apply cho moi don du dieu kien, false = chi khi nhap code */
    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private Boolean isPublic = false;
}
