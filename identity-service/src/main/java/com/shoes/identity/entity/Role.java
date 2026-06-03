package com.shoes.identity.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Role entity — dinh nghia quyen trong he thong.
 * Cac role mac dinh: ADMIN, USER, STAFF (seed luc khoi tao app).
 * Tach Role ra entity rieng (thay vi JSON column nhu monolith) de:
 *   - De them moi role
 *   - De truy van "user thuoc role X"
 *   - Co the gan metadata (description) cho moi role
 */
@Entity
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(name = "uk_role_name", columnNames = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "id")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ten role: "ADMIN", "USER", "STAFF" — unique */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /** Mo ta ngan gon — optional */
    @Column(name = "description", length = 255)
    private String description;
}
