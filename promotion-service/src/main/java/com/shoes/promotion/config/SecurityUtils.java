package com.shoes.promotion.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Utility lay thong tin user dang dang nhap tu SecurityContext.
 *
 * Tien de: JwtAuthenticationFilter da chay truoc va set principal = userId (Long).
 * Goi tu BAT KY layer nao (service / controller / aspect).
 *
 * Vi du:
 *   Long userId = SecurityUtils.getCurrentUserId();
 *   if (SecurityUtils.hasRole("ADMIN")) { ... }
 */
public final class SecurityUtils {

    private SecurityUtils() {}

    /** @return userId cua user dang login. Throw neu chua login. */
    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            throw new IllegalStateException("Chua dang nhap — khong co user trong SecurityContext");
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof Long) {
            return (Long) principal;
        }
        if (principal instanceof Number) {
            return ((Number) principal).longValue();
        }
        // Fallback — neu principal la String (vd "anonymousUser") thi parse loi
        try {
            return Long.parseLong(principal.toString());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Principal khong phai userId Long: " + principal);
        }
    }

    /** Tra ve null neu chua login (KHONG throw) — dung khi endpoint optional auth. */
    public static Long getCurrentUserIdOrNull() {
        try {
            return getCurrentUserId();
        } catch (IllegalStateException e) {
            return null;
        }
    }

    /** Lay danh sach role cua user hien tai (vd: ["ADMIN", "USER"]). */
    public static Collection<String> getCurrentRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities() == null) return Collections.emptyList();
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(a -> a.startsWith("ROLE_") ? a.substring(5) : a)
                .collect(Collectors.toList());
    }

    /** Check user co role X (so sanh khong phan biet hoa thuong). */
    public static boolean hasRole(String role) {
        return getCurrentRoles().stream().anyMatch(r -> r.equalsIgnoreCase(role));
    }

    public static boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(String.valueOf(auth.getPrincipal()));
    }
}
