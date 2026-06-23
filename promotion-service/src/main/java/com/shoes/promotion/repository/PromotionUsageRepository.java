package com.shoes.promotion.repository;

import com.shoes.promotion.entity.PromotionUsage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionUsageRepository extends JpaRepository<PromotionUsage, Long> {

    long countByPromotionIdAndUserId(Long promotionId, Long userId);

    Optional<PromotionUsage> findByOrderId(Long orderId);

    Page<PromotionUsage> findByUserId(Long userId, Pageable pageable);
}