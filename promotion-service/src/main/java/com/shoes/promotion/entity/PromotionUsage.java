package com.shoes.promotion.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Lich su user dung coupon — ghi nhan moi lan apply thanh cong.
 * Dung de:
 *   - Check user da dung coupon X bao nhieu lan (chong abuse khi co usage_limit_per_user)
 *   - Audit: ai dung khi nao, giam bao nhieu
 *   - Rollback: khi don bi huy, co the rollback used_count
 *
 * KHONG FK den orders (cross-service) → chi luu order_id la Long.
 */
@Entity
@Table(name = "promotion_usages",
        indexes = {
                @Index(name = "idx_usage_promo_user", columnList = "promotion_id, user_id"),
                @Index(name = "idx_usage_order", columnList = "order_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "id")
public class PromotionUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_usage_promotion"))
    private Promotion promotion;

    /** User da apply coupon — ref den identity-service (KHONG FK cross-DB) */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** Order da apply — ref den order-service */
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    /** So tien giam thuc te (VND) — snapshot tai thoi diem apply */
    @Column(name = "discount_applied", nullable = false)
    private Long discountApplied;

    @Column(name = "used_at", nullable = false)
    @Builder.Default
    private LocalDateTime usedAt = LocalDateTime.now();
}
