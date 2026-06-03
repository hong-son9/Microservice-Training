package com.shoes.cart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Cart aggregate root — moi user co 1 gio hang duy nhat.
 *
 * user_id ref den identity-service (Long, KHONG FK cross-DB).
 * CartItem la part of Cart aggregate → cascade ALL + orphanRemoval.
 */
@Entity
@Table(name = "carts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cart_user", columnNames = "user_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "items")
@EqualsAndHashCode(of = "id", callSuper = false)
public class Cart extends BaseEntity {

    /** Chu so huu cart — ref den identity-service.users.id */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    /* ===== Helpers ===== */

    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
    }

    /** Tong tien tam tinh (chua tru promotion) */
    public long calcSubtotal() {
        return items.stream()
                .mapToLong(i -> (i.getUnitPrice() == null ? 0L : i.getUnitPrice()) * i.getQuantity())
                .sum();
    }

    public int totalQuantity() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }
}
