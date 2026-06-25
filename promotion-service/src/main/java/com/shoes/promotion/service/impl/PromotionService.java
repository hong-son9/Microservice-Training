package com.shoes.promotion.service.impl;

import com.shoes.promotion.dto.PromotionDTO;
import com.shoes.promotion.dto.PromotionValidateResultDTO;
import com.shoes.promotion.entity.DiscountType;
import com.shoes.promotion.entity.Promotion;
import com.shoes.promotion.repository.PromotionRepository;
import com.shoes.promotion.repository.PromotionUsageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionUsageRepository usageRepository;

    @Transactional
    public Page<PromotionDTO> getAllPromotions(Pageable pageable) {
        return promotionRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional
    public PromotionDTO getPromotionById(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chương trình khuyến mãi với ID: " + id));
        return convertToDTO(promotion);
    }

    @Transactional
    public PromotionDTO createPromotion(PromotionDTO dto) {
        if (promotionRepository.existsByCouponCode(dto.getCouponCode())) {
            throw new IllegalArgumentException("Mã giảm giá '" + dto.getCouponCode() + "' đã tồn tại!");
        }
        Promotion promotion = new Promotion();
        BeanUtils.copyProperties(dto, promotion, "id", "usedCount");
        promotion = promotionRepository.save(promotion);
        return convertToDTO(promotion);
    }

    @Transactional
    public PromotionDTO updatePromotion(Long id, PromotionDTO dto) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chương trình khuyến mãi để cập nhật"));

        // Nếu đổi mã code, phải check trùng
        if (!promotion.getCouponCode().equalsIgnoreCase(dto.getCouponCode())
                && promotionRepository.existsByCouponCode(dto.getCouponCode())) {
            throw new IllegalArgumentException("Mã giảm giá '" + dto.getCouponCode() + "' đã tồn tại!");
        }

        BeanUtils.copyProperties(dto, promotion, "id", "usedCount");
        promotion = promotionRepository.save(promotion);
        return convertToDTO(promotion);
    }

    @Transactional
    public void deletePromotion(Long id) {
        if (!promotionRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy chương trình khuyến mãi để xóa");
        }
        promotionRepository.deleteById(id);
    }

    private PromotionDTO convertToDTO(Promotion promotion) {
        PromotionDTO dto = new PromotionDTO();
        BeanUtils.copyProperties(promotion, dto);
        return dto;
    }

    @Transactional
    public PromotionValidateResultDTO validate(Long id, Long userId, Long orderAmount) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chương trình khuyến mãi với ID: " + id));

        LocalDateTime now = LocalDateTime.now();

        if (!promotion.getIsActive()) {
            return PromotionValidateResultDTO.builder()
                    .isValid(false)
                    .reasonCode("NOT_ACTIVE")
                    .message("Mã giảm giá này đã bị vô hiệu hóa.")
                    .discountAmount(0L)
                    .build();
        }

        if (promotion.getStartedAt() != null && now.isBefore(promotion.getStartedAt())) {
            return PromotionValidateResultDTO.builder()
                    .isValid(false)
                    .reasonCode("NOT_STARTED")
                    .message("Chương trình khuyến mãi chưa bắt đầu.")
                    .discountAmount(0L)
                    .build();
        }

        if (promotion.getExpiredAt() != null && now.isAfter(promotion.getExpiredAt())) {
            return PromotionValidateResultDTO.builder()
                    .isValid(false)
                    .reasonCode("EXPIRED")
                    .message("Mã giảm giá đã hết hạn sử dụng.")
                    .discountAmount(0L)
                    .build();
        }

        if (promotion.getUsageLimit() != null && promotion.getUsedCount() >= promotion.getUsageLimit()) {
            return PromotionValidateResultDTO.builder()
                    .isValid(false)
                    .reasonCode("OUT_OF_STOCK")
                    .message("Mã giảm giá đã hết lượt sử dụng trên hệ thống.")
                    .discountAmount(0L)
                    .build();
        }

        if (promotion.getUsageLimitPerUser() != null) {
            // Bạn cần viết thêm câu query count trong PromotionUsageRepository hoặc OrderRepository tùy thiết kế của bạn
            long userUsedCount = usageRepository.countByPromotionIdAndUserId(id, userId);
            if (userUsedCount >= promotion.getUsageLimitPerUser()) {
                return PromotionValidateResultDTO.builder()
                        .isValid(false)
                        .reasonCode("USER_LIMIT_REACHED")
                        .message("Bạn đã sử dụng hết lượt cho mã giảm giá này.")
                        .discountAmount(0L)
                        .build();
            }
        }

        // 7. Kiểm tra giá trị đơn hàng tối thiểu (minOrderValue)
        if (promotion.getMinOrderValue() != null && orderAmount < promotion.getMinOrderValue()) {
            return PromotionValidateResultDTO.builder()
                    .isValid(false)
                    .reasonCode("MIN_VALUE_NOT_MET")
                    .message("Đơn hàng chưa đạt giá trị tối thiểu để áp dụng mã này (Tối thiểu từ " + promotion.getMinOrderValue() + "đ).")
                    .discountAmount(0L)
                    .build();
        }

        // 8. Tính toán số tiền được giảm (Discount Amount) nếu tất cả điều kiện trên đều HỢP LỆ
        long discountAmount = 0L;
        if (promotion.getDiscountType() == DiscountType.FIXED) {
            // Loại giảm tiền cố định
            discountAmount = promotion.getDiscountValue();
        } else if (promotion.getDiscountType() == DiscountType.PERCENTAGE) {
            // Loại giảm theo phần trăm: (Giá trị đơn * % giảm) / 100
            discountAmount = (orderAmount * promotion.getDiscountValue()) / 100;

            // Nếu có cấu hình số tiền giảm tối đa (maximumDiscountValue) thì chặn lại
            if (promotion.getMaximumDiscountValue() != null && discountAmount > promotion.getMaximumDiscountValue()) {
                discountAmount = promotion.getMaximumDiscountValue();
            }
        }

        // Đảm bảo số tiền giảm không vượt quá tổng giá trị đơn hàng (tránh tiền âm)
        if (discountAmount > orderAmount) {
            discountAmount = orderAmount;
        }

        // 9. Trả về kết quả HỢP LỆ thành công
        return PromotionValidateResultDTO.builder()
                .isValid(true)
                .reasonCode("SUCCESS")
                .message("Áp dụng mã giảm giá thành công!")
                .discountAmount(discountAmount)
                .build();
    }

    @Transactional
    public void cancelPromotion(Long promotionId, Long userId, Long orderId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chương trình khuyến mãi với ID: " + promotionId));

        usageRepository.deleteByUserIdAndPromotionIdAndOrderId(userId, promotionId, orderId);

        int rowsAffected = promotionRepository.decrementUsedCount(promotionId);

        if (rowsAffected == 0) {
            log.warn("Không thể giảm usedCount cho Promotion ID: {} vì usedCount đã bằng 0", promotionId);
        }
    }
}
