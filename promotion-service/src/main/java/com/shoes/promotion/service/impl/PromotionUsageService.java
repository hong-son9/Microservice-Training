package com.shoes.promotion.service.impl;

import com.shoes.promotion.dto.PromotionUsageDTO;
import com.shoes.promotion.dto.PromotionUsageResponse;
import com.shoes.promotion.entity.Promotion;
import com.shoes.promotion.entity.PromotionUsage;
import com.shoes.promotion.repository.PromotionRepository;
import com.shoes.promotion.repository.PromotionUsageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PromotionUsageService {

    private final PromotionUsageRepository usageRepository;
    private final PromotionRepository promotionRepository;

    // 1. Áp dụng mã (Mấu chốt xử lý logic kiểm tra & tăng số lần dùng)
    @Transactional
    public PromotionUsageDTO applyPromotion(String couponCode, Long userId, Long orderId, Long orderValue) {
        // Khóa dòng dữ liệu để tránh race condition
        Promotion promotion = promotionRepository.findByCouponCodeForUpdate(couponCode)
                .orElseThrow(() -> new IllegalArgumentException("Mã giảm giá không tồn tại"));

        // Validate cơ bản
        if (!promotion.getIsActive()) throw new IllegalStateException("Mã giảm giá đã bị vô hiệu hóa");
        if (LocalDateTime.now().isBefore(promotion.getStartedAt())) throw new IllegalStateException("Mã chưa đến thời gian sử dụng");
        if (LocalDateTime.now().isAfter(promotion.getExpiredAt())) throw new IllegalStateException("Mã đã hết hạn");
        if (promotion.getMinOrderValue() != null && orderValue < promotion.getMinOrderValue()) {
            throw new IllegalStateException("Đơn hàng chưa đạt giá trị tối thiểu: " + promotion.getMinOrderValue());
        }
        if (promotion.getUsageLimit() != null && promotion.getUsedCount() >= promotion.getUsageLimit()) {
            throw new IllegalStateException("Mã giảm giá đã hết lượt sử dụng hệ thống");
        }

        // Kiểm tra số lần dùng của từng User
        if (promotion.getUsageLimitPerUser() != null) {
            long userUsageCount = usageRepository.countByPromotionIdAndUserId(promotion.getId(), userId);
            if (userUsageCount >= promotion.getUsageLimitPerUser()) {
                throw new IllegalStateException("Bạn đã dùng hết lượt cho phép của mã này");
            }
        }

        // Tính số tiền giảm thực tế dựa vào Loại Discount
        long discountApplied = 0;
        switch (promotion.getDiscountType()) {
            case FIXED:
                discountApplied = promotion.getDiscountValue();
                break;
            case PERCENTAGE:
                discountApplied = (orderValue * promotion.getDiscountValue()) / 100;
                if (promotion.getMaximumDiscountValue() != null && discountApplied > promotion.getMaximumDiscountValue()) {
                    discountApplied = promotion.getMaximumDiscountValue();
                }
                break;
        }

        // Cập nhật lượt dùng của coupon
        promotion.setUsedCount(promotion.getUsedCount() + 1);
        promotionRepository.save(promotion);

        // Ghi nhận lịch sử sử dụng
        PromotionUsage usage = PromotionUsage.builder()
                .promotion(promotion)
                .userId(userId)
                .orderId(orderId)
                .discountApplied(discountApplied)
                .build();
        usage = usageRepository.save(usage);

        return convertToDTO(usage);
    }

    // 2. Rollback mã khi đơn hàng bị hủy
    @Transactional
    public void rollbackPromotion(Long orderId) {
        PromotionUsage usage = usageRepository.findByOrderId(orderId).orElse(null);
        if (usage != null) {
            promotionRepository.decrementUsedCount(usage.getPromotion().getId());
            usageRepository.delete(usage);
        }
    }

    @Transactional
    public Page<PromotionUsageDTO> getHistoryByUserId(Long userId, Pageable pageable) {
        return usageRepository.findByUserId(userId, pageable).map(this::convertToDTO);
    }

    private PromotionUsageDTO convertToDTO(PromotionUsage usage) {
        PromotionUsageDTO dto = new PromotionUsageDTO();
        BeanUtils.copyProperties(usage, dto);
        dto.setPromotionId(usage.getPromotion().getId());
        dto.setCouponCode(usage.getPromotion().getCouponCode());
        return dto;
    }

    @Transactional
    public PromotionUsageResponse applyPromotionById(Long promotionId, Long userId, Long orderId, Long orderValue) {

        // 1. Tìm kiếm kết hợp Khóa dòng (Lock ghi) để chặn luồng đồng thời sửa usedCount trái phép
        Promotion promotion = promotionRepository.findByIdForUpdate(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("Chương trình khuyến mãi không tồn tại"));

        // 2. Kiểm tra tính hợp lệ của Mã
        if (!promotion.getIsActive()) {
            throw new IllegalStateException("Mã giảm giá đã bị vô hiệu hóa hoặc tạm dừng");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(promotion.getStartedAt())) {
            throw new IllegalStateException("Chương trình khuyến mãi chưa bắt đầu");
        }
        if (now.isAfter(promotion.getExpiredAt())) {
            throw new IllegalStateException("Mã giảm giá đã hết hạn sử dụng");
        }

        // 3. Kiểm tra điều kiện giá trị đơn hàng tối thiểu
        if (promotion.getMinOrderValue() != null && orderValue < promotion.getMinOrderValue()) {
            throw new IllegalStateException("Đơn hàng chưa đạt giá trị tối thiểu " + promotion.getMinOrderValue() + "đ để áp dụng mã");
        }

        // 4. Kiểm tra giới hạn tổng số lần dùng của hệ thống
        if (promotion.getUsageLimit() != null && promotion.getUsedCount() >= promotion.getUsageLimit()) {
            throw new IllegalStateException("Mã giảm giá này đã hết lượt sử dụng hệ thống");
        }

        // 5. Kiểm tra số lần dùng tối đa của chính User đó
        if (promotion.getUsageLimitPerUser() != null) {
            long userUsageCount = usageRepository.countByPromotionIdAndUserId(promotionId, userId);
            if (userUsageCount >= promotion.getUsageLimitPerUser()) {
                throw new IllegalStateException("Bạn đã sử dụng hết lượt cho phép của mã giảm giá này");
            }
        }

        // 6. Tính toán số tiền được giảm thực tế
        long discountApplied = 0;
        switch (promotion.getDiscountType()) {
            case FIXED:
                discountApplied = promotion.getDiscountValue();
                break;

            case PERCENTAGE:
                // Tính % theo giá trị đơn hàng
                discountApplied = (orderValue * promotion.getDiscountValue()) / 100;

                // Cắt ngọn nếu vượt quá số tiền giảm tối đa cấu hình
                if (promotion.getMaximumDiscountValue() != null && discountApplied > promotion.getMaximumDiscountValue()) {
                    discountApplied = promotion.getMaximumDiscountValue();
                }
                break;
        }

        // Đảm bảo số tiền giảm không vượt quá cả giá trị đơn hàng hiện tại
        if (discountApplied > orderValue) {
            discountApplied = orderValue;
        }

        // 7. Cập nhật tăng số lần đã sử dụng (usedCount) của mã
        promotion.setUsedCount(promotion.getUsedCount() + 1);
        promotionRepository.save(promotion);

        // 8. Lưu lại snapshot lịch sử sử dụng vào bảng promotion_usages
        PromotionUsage usage = PromotionUsage.builder()
                .promotion(promotion)
                .userId(userId)
                .orderId(orderId)
                .discountApplied(discountApplied)
                .usedAt(now)
                .build();
        usage = usageRepository.save(usage);

        // 9. Trả về kết quả gọn nhẹ cho Order-Service tính tiền tiếp
        return PromotionUsageResponse.builder()
                .id(usage.getId())
                .discountApplied(discountApplied)
                .build();
    }
}