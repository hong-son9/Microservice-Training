package com.shoes.promotion.service.impl;

import com.shoes.promotion.dto.PromotionDTO;
import com.shoes.promotion.entity.Promotion;
import com.shoes.promotion.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;

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
}
