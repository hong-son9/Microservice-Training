package com.shoes.identity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * User entity — aggregate root cua bounded context Identity.
 *
 * Quy uoc:
 * - email unique tren toan he thong (login bang email)
 * - password BCrypt hash (NULL voi user OAuth chua set password)
 * - status: true=active, false=da khoa (admin disable)
 * - provider+providerId: track nguon dang ky (LOCAL/GOOGLE/FACEBOOK)
 * - roles: Many-to-Many voi Role qua bang user_roles
 *
 * KHONG luu thong tin nhay cam khac (CCCD, the tin dung...) o day.
 * Cart/Order/Payment chi tham chieu user qua id, KHONG join cross-DB.
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_email", columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_user_phone", columnList = "phone"),
                @Index(name = "idx_user_provider", columnList = "provider, provider_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"password", "roles"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class User extends BaseEntity {

    @Column(name = "email", nullable = false, length = 200)
    private String email;

    /** BCrypt hash — null neu user dang ky bang OAuth chua co password */
    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "full_name", length = 150)
    private String fullName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "avatar", length = 500)
    private String avatar;

    /** true=active (login duoc), false=bi admin khoa */
    @Column(name = "status", nullable = false)
    @Builder.Default
    private Boolean status = true;

    /** LOCAL / GOOGLE / FACEBOOK */
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 20)
    @Builder.Default
    private AuthProvider provider = AuthProvider.LOCAL;

    /** ID cua user ben Google/FB (chi co voi OAuth) */
    @Column(name = "provider_id", length = 100)
    private String providerId;

    /** Email da xac thuc qua OTP chua */
    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    /** Many-to-Many: user → roles. Eager fetch vi auth check role lien tuc */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = @UniqueConstraint(
                    name = "uk_user_role", columnNames = {"user_id", "role_id"}
            )
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    /* ===== Helper methods ===== */

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(r -> r.getName().equalsIgnoreCase(roleName));
    }
}
