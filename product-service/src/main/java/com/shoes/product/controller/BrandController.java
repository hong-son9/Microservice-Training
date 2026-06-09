package com.shoes.product.controller;

import com.shoes.product.dto.ApiResponse;
import com.shoes.product.dto.Request.CreateBrandRequest;
import com.shoes.product.dto.Request.UpdateBrandRequest;
import com.shoes.product.dto.Response.BrandResponse;
import com.shoes.product.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Brand endpoints
 * Follows REST conventions with proper HTTP status codes
 * All responses wrapped in ApiResponse
 */
@RestController
@RequestMapping("/api/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * CREATE - POST /api/brands
     * Requires ADMIN role
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<BrandResponse>> create(@Valid @RequestBody CreateBrandRequest request) {
        BrandResponse response = brandService.create(request);
        return new ResponseEntity<>(
                ApiResponse.created(response, "Brand created successfully"),
                HttpStatus.CREATED
        );
    }

    /**
     * READ - GET /api/brands/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> getById(@PathVariable("id") Long id) {
        BrandResponse response = brandService.getById(id);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Brand retrieved successfully")
        );
    }

    /**
     * READ - GET /api/brands
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAll() {
        List<BrandResponse> responses = brandService.getAll();
        return ResponseEntity.ok(
                ApiResponse.success(responses, "Brands retrieved successfully")
        );
    }

    /**
     * READ - GET /api/brands/active
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAllActive() {
        List<BrandResponse> responses = brandService.getAllActive();
        return ResponseEntity.ok(
                ApiResponse.success(responses, "Active brands retrieved successfully")
        );
    }

    /**
     * UPDATE - PUT /api/brands/{id}
     * Requires ADMIN role
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<BrandResponse>> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateBrandRequest request) {
        BrandResponse response = brandService.update(id, request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Brand updated successfully")
        );
    }

    /**
     * DELETE - DELETE /api/brands/{id}
     * Soft delete (mark as deleted, preserve data)
     * Requires ADMIN role
     */
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<ApiResponse<String>> delete(@PathVariable("id") Long id) {
//        brandService.delete(id);
//        return ResponseEntity.ok(
//                ApiResponse.success("Brand deleted successfully")
//        );
//    }

    /**
     * DELETE - DELETE /api/brands/{id}?hard=true
     * Hard delete (permanent deletion)
     * Requires ADMIN role
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteHard(
            @PathVariable("id") Long id,
            @RequestParam(value = "hard", defaultValue = "false") boolean hard) {
        if (hard) {
            brandService.deleteHard(id);
            return ResponseEntity.ok(ApiResponse.success("Brand permanently deleted"));
        } else {
            brandService.delete(id);
            return ResponseEntity.ok(ApiResponse.success("Brand deleted successfully"));
        }
    }
}
