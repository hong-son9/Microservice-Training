package com.shoes.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Order aggregate root.
 *
 * Cross-service refs (Long, KHONG FK):
 *   - buyer_user_id → identity-service.users.id
 *   - items[].product_id → product-service.products.id
 *   - promotion_id → promotion-service.promotions.id (nullable)
 *
 * Aggregate-internal:
 *   - items (OneToMany OrderItem) — cascade ALL
 *   - statusHistory (OneToMany OrderStatusHistory) — cascade ALL
 *
 * Pricing:
 *   - subtotal       = sum(item.line_total)
 *   - discount_amount = tien giam tu promotion
 *   - shipping_fee   = phi ship
 *   - total          = subtotal - discount_amount + shipping_fee
 */
@Entity
@Table(name = "orders",
        indexes = {
                @Index(name = "idx_order_buyer", columnList = "buyer_user_id"),
                @Index(name = "idx_order_status", columnList = "status"),
                @Index(name = "idx_order_created", columnList = "created_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"items", "statusHistory"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ma don hien thi cho khach — VD: "SH-20260603-00123" */
    @Column(name = "order_code", nullable = false, length = 30, unique = true)
    private String orderCode;

    /** User dat hang — ref identity-service */
    @Column(name = "buyer_user_id", nullable = false)
    private Long buyerUserId;

    /* ===== Thong tin nguoi nhan (snapshot tu user profile / form checkout) ===== */
    @Column(name = "receiver_name", nullable = false, length = 150)
    private String receiverName;

    @Column(name = "receiver_phone", nullable = false, length = 20)
    private String receiverPhone;

    @Column(name = "receiver_address", nullable = false, length = 500)
    private String receiverAddress;

    @Column(name = "note", length = 500)
    private String note;

    /* ===== Pricing ===== */

    @Column(name = "subtotal", nullable = false)
    private Long subtotal;

    @Column(name = "discount_amount", nullable = false)
    @Builder.Default
    private Long discountAmount = 0L;

    @Column(name = "shipping_fee", nullable = false)
    @Builder.Default
    private Long shippingFee = 0L;

    @Column(name = "total", nullable = false)
    private Long total;

    /* ===== Promotion snapshot ===== */

    /** Ref promotion-service.promotions.id (nullable) */
    @Column(name = "promotion_id")
    private Long promotionId;

    /** Snapshot ma coupon — neu admin xoa coupon, don van con thong tin */
    @Column(name = "promotion_code", length = 50)
    private String promotionCode;

    /* ===== Status + Payment ===== */

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    @Builder.Default
    private PaymentMethod paymentMethod = PaymentMethod.COD;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    /** Transaction id tu VNPay/Momo (chi co khi paid) */
    @Column(name = "payment_transaction_id", length = 100)
    private String paymentTransactionId;

    /* ===== Aggregate-internal relations ===== */

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderStatusHistory> statusHistory = new ArrayList<>();

    /* ===== Helpers ===== */

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void appendHistory(OrderStatusHistory h) {
        statusHistory.add(h);
        h.setOrder(this);
    }
}
