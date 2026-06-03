package com.shoes.product.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Size variant cua 1 san pham — moi product co nhieu size, moi size co kho rieng.
 * KHONG ke thua BaseEntity vi day la child cua Product aggregate, audit lay theo Product.
 */
@Entity
@Table(name = "product_sizes",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_product_size", columnNames = {"product_id", "size_vn"})
        },
        indexes = {
                @Index(name = "idx_size_product", columnList = "product_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "product")
@EqualsAndHashCode(of = "id")
public class ProductSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_size_product"))
    private Product product;

    /** Size chuan VN: 35, 36, ..., 42 */
    @Column(name = "size_vn", nullable = false)
    private Integer sizeVn;

    /** So luong ton kho cho size nay */
    @Column(name = "quantity", nullable = false)
    @Builder.Default
    private Integer quantity = 0;
}
