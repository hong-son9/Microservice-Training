package com.shoes.promotion.controller;

import com.shoes.promotion.dto.PromotionDTO;
import com.shoes.promotion.service.impl.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping
    public ResponseEntity<Page<PromotionDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(promotionService.getAllPromotions(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(promotionService.getPromotionById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PromotionDTO> create(@RequestBody PromotionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionService.createPromotion(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PromotionDTO> update(@PathVariable("id") Long id, @RequestBody PromotionDTO dto) {
        return ResponseEntity.ok(promotionService.updatePromotion(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }
}