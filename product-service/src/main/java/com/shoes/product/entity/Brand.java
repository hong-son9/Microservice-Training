package com.shoes.product.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Brand entity — nhan hieu giay (Nike, Adidas, Puma...).
 */
@Entity
@Table(name = "brands",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_brand_slug", columnNames = "slug")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
public class Brand extends BaseEntity {

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /** URL-friendly slug (vd: "nike", "adidas-yeezy") */
    @Column(name = "slug", nullable = false, length = 200)
    private String slug;

    @Column(name = "logo", length = 500)
    private String logo;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /** true=hien thi, false=an tren shop */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
