package com.shoes.product.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Anh san pham — gallery + main image.
 * Cho phep N anh per product, sort_order de hien thi theo thu tu.
 */
@Entity
@Table(name = "product_images",
        indexes = {
                @Index(name = "idx_image_product", columnList = "product_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "product")
@EqualsAndHashCode(of = "id")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_image_product"))
    private Product product;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    /** Anh chinh (hien thi o card san pham + tooltip) */
    @Column(name = "is_main", nullable = false)
    @Builder.Default
    private Boolean isMain = false;

    /** Sap xep trong gallery */
    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;
}
