package com.shoes.promotion.repository;

import com.shoes.promotion.entity.Promotion;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    Optional<Promotion> findByCouponCode(String couponCode);

    boolean existsByCouponCode(String couponCode);

    // Dùng lock để tránh race condition khi nhiều user cùng áp mã một lúc
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Promotion p WHERE p.couponCode = :code")
    Optional<Promotion> findByCouponCodeForUpdate(@Param("code") String code);

    @Modifying
    @Query("UPDATE Promotion p SET p.usedCount = p.usedCount + 1 WHERE p.id = :id AND (p.usageLimit IS NULL OR p.usedCount < p.usageLimit)")
    int incrementUsedCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Promotion p SET p.usedCount = p.usedCount - 1 WHERE p.id = :id AND p.usedCount > 0")
    int decrementUsedCount(@Param("id") Long id);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Promotion p WHERE p.id = :id")
    Optional<Promotion> findByIdForUpdate(@Param("id") Long id);
}
