package com.shoes.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Audit trail moi lan order doi status — phuc vu:
 *   - User track don (vd: "lon dau dat thoi diem nao", "khi nao xac nhan", ...)
 *   - Admin xem ai da update don
 *   - Debug: tai sao don ket o trang thai X
 */
@Entity
@Table(name = "order_status_history",
        indexes = {
                @Index(name = "idx_history_order", columnList = "order_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "order")
@EqualsAndHashCode(of = "id")
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_history_order"))
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 20)
    private OrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, length = 20)
    private OrderStatus toStatus;

    /** User id thuc hien thay doi (admin / chinh khach huy don) — ref identity */
    @Column(name = "changed_by_user_id")
    private Long changedByUserId;

    /** Ghi chu — vd "Khach huy vi doi y", "Da goi xac nhan qua dien thoai" */
    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "changed_at", nullable = false)
    @Builder.Default
    private LocalDateTime changedAt = LocalDateTime.now();
}
