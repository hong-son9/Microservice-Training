package com.shoes.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Product aggregate root.
 *
 * Relationship:
 *   - ManyToOne Brand
 *   - ManyToMany Category (1 san pham co the thuoc nhieu danh muc)
 *   - OneToMany ProductSize (kho theo size — aggregate-internal)
 *   - OneToMany ProductImage (gallery — aggregate-internal)
 *
 * Cascade ALL + orphanRemoval cho size/image vi chung la PART of Product aggregate.
 */
@Entity
@Table(name = "products",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_product_slug", columnNames = "slug"),
                @UniqueConstraint(name = "uk_product_sku", columnNames = "sku")
        },
        indexes = {
                @Index(name = "idx_product_brand", columnList = "brand_id"),
                @Index(name = "idx_product_status", columnList = "status"),
                @Index(name = "idx_product_name", columnList = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"sizes", "images", "categories"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class Product extends BaseEntity {

    @Column(name = "sku", nullable = false, length = 50)
    private String sku;

    @Column(name = "name", nullable = false, length = 300)
    private String name;

    @Column(name = "slug", nullable = false, length = 350)
    private String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /** Gia niem yet (VND) */
    @Column(name = "price", nullable = false)
    private Long price;

    /** Gia khuyen mai (VND) — null neu khong sale */
    @Column(name = "sale_price")
    private Long salePrice;

    /** Gia von (VND) — chi admin xem, dung tinh loi nhuan */
    @Column(name = "import_price")
    private Long importPrice;

    /** So lan view (tang khi user xem chi tiet) */
    @Column(name = "view_count", nullable = false)
    @Builder.Default
    private Long viewCount = 0L;

    /** Tong so doi da ban (cap nhat khi don COMPLETED) */
    @Column(name = "total_sold", nullable = false)
    @Builder.Default
    private Long totalSold = 0L;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ProductStatus status = ProductStatus.DRAFT;

    /* ===== Relationships ===== */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", foreignKey = @ForeignKey(name = "fk_product_brand"))
    private Brand brand;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"),
            uniqueConstraints = @UniqueConstraint(
                    name = "uk_product_category", columnNames = {"product_id", "category_id"}
            )
    )
    @Builder.Default
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductSize> sizes = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    /* ===== Helpers ===== */

    public void addSize(ProductSize size) {
        sizes.add(size);
        size.setProduct(this);
    }

    public void addImage(ProductImage image) {
        images.add(image);
        image.setProduct(this);
    }

    /** Gia hieu luc: salePrice neu co (< price), nguoc lai la price */
    public long effectivePrice() {
        if (salePrice != null && salePrice > 0 && salePrice < price) {
            return salePrice;
        }
        return price;
    }
}
