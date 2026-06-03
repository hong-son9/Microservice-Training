package com.shoes.cart.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * CartItem — 1 dong trong gio (san pham + size + quantity).
 *
 * SNAPSHOT pattern: luu product_name, product_image, unit_price tai thoi diem add cart.
 * Ly do:
 *   1. Khong can Feign call product-service moi lan load gio hang (performance)
 *   2. Lo product doi gia/ten sau khi add → cart van giu thong tin cu (UX)
 *
 * Sync snapshot khi nao?
 *   - Khong tu dong sync (don gian cho training)
 *   - Refresh thu cong: user click "Cap nhat gio" → Feign goi product-service
 *   - Production: lang nghe event "product.updated" qua Kafka → sync
 */
@Entity
@Table(name = "cart_items",
        uniqueConstraints = {
                // 1 cart khong duoc co 2 dong cung product + same size → bat user phai +qty
                @UniqueConstraint(name = "uk_cart_product_size",
                        columnNames = {"cart_id", "product_id", "size_vn"})
        },
        indexes = {
                @Index(name = "idx_cartitem_cart", columnList = "cart_id"),
                @Index(name = "idx_cartitem_product", columnList = "product_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "cart")
@EqualsAndHashCode(of = "id")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_cart"))
    private Cart cart;

    /** Ref den product-service.products.id */
    @Column(name = "product_id", nullable = false)
    private Long productId;

    /** Size cu the cua product (35-42) */
    @Column(name = "size_vn", nullable = false)
    private Integer sizeVn;

    @Column(name = "quantity", nullable = false)
    @Builder.Default
    private Integer quantity = 1;

    /* ===== Snapshot fields — tu product-service luc add cart ===== */

    @Column(name = "product_name", nullable = false, length = 300)
    private String productName;

    @Column(name = "product_slug", length = 350)
    private String productSlug;

    @Column(name = "product_image", length = 500)
    private String productImage;

    /** Gia tai thoi diem add cart (VND) — locked snapshot */
    @Column(name = "unit_price", nullable = false)
    private Long unitPrice;

    /** Tong tien dong nay = unit_price * quantity (computed khi save) */
    public long subTotal() {
        return (unitPrice == null ? 0L : unitPrice) * (quantity == null ? 0 : quantity);
    }
}
