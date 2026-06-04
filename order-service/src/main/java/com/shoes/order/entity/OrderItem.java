package com.shoes.order.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * OrderItem — 1 dong san pham trong don.
 * SNAPSHOT toan bo thong tin product luc dat: ten, anh, gia, size.
 * Don da dat xong KHONG bao gio refetch tu product-service — luc do don hang la "frozen".
 */
@Entity
@Table(name = "order_items",
        indexes = {
                @Index(name = "idx_orderitem_order", columnList = "order_id"),
                @Index(name = "idx_orderitem_product", columnList = "product_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "order")
@EqualsAndHashCode(of = "id")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_order"))
    private Order order;

    /** Ref product-service.products.id */
    @Column(name = "product_id", nullable = false)
    private Long productId;

    /** Snapshot — luon co du de hien thi khi product bi xoa o product-service */
    @Column(name = "product_name", nullable = false, length = 300)
    private String productName;

    @Column(name = "product_slug", length = 350)
    private String productSlug;

    @Column(name = "product_image", length = 500)
    private String productImage;

    @Column(name = "product_sku", length = 50)
    private String productSku;

    /** Size VN da dat (35-42) */
    @Column(name = "size_vn", nullable = false)
    private Integer sizeVn;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /** Gia 1 doi luc dat (sau khuyen mai san pham, truoc coupon) */
    @Column(name = "unit_price", nullable = false)
    private Long unitPrice;

    /** Tong tien dong nay = unit_price * quantity. Snapshot tai thoi diem dat. */
    @Column(name = "line_total", nullable = false)
    private Long lineTotal;
}
