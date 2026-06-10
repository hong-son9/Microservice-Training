package com.shoes.product.controller;

import com.shoes.product.dto.ApiResponse;
import com.shoes.product.dto.Request.CreateProductRequest;
import com.shoes.product.dto.Request.UpdateProductRequest;
import com.shoes.product.dto.Response.PagedResponse;
import com.shoes.product.dto.Response.ProductResponse;
import com.shoes.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Product endpoints
 * Follows REST conventions with proper HTTP status codes
 * All responses wrapped in ApiResponse
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * CREATE - POST /api/products
     * Create a new product
     * Requires ADMIN role
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.create(request);
        return new ResponseEntity<>(
                ApiResponse.created(response, "Product created successfully"),
                HttpStatus.CREATED
        );
    }

    /**
     * READ - GET /api/products/{id}
     * Get product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable("id") Long id) {
        ProductResponse response = productService.getById(id);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Product retrieved successfully")
        );
    }

    /**
     * READ - GET /api/products
     * Get all products (non-deleted)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAll() {
        List<ProductResponse> responses = productService.getAll();
        return ResponseEntity.ok(
                ApiResponse.success(responses, "Products retrieved successfully")
        );
    }

    /**
     * READ - GET /api/products/active
     * Get only active products
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllActive() {
        List<ProductResponse> responses = productService.getAllActive();
        return ResponseEntity.ok(
                ApiResponse.success(responses, "Active products retrieved successfully")
        );
    }

    /**
     * READ - GET /api/products/snapshots?ids=1,2,3
     * Get products by list of IDs (for snapshots in order service)
     */
    @GetMapping("/snapshots")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByIds(@RequestParam("ids") List<Long> ids) {
        List<ProductResponse> responses = productService.getAllById(ids);
        return ResponseEntity.ok(
                ApiResponse.success(responses, "Product snapshots retrieved successfully")
        );
    }

    /**
     * UPDATE - PUT /api/products/{id}
     * Update existing product
     * Requires ADMIN role
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        ProductResponse response = productService.update(id, request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Product updated successfully")
        );
    }

    /**
     * DELETE - DELETE /api/products/{id}
     * Soft delete product (mark as deleted, preserve data)
     * Requires ADMIN role
     */
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<ApiResponse<String>> delete(@PathVariable("id") Long id) {
//        productService.delete(id);
//        return ResponseEntity.ok(
//                ApiResponse.success("Product deleted successfully")
//        );
//    }

    /**
     * DELETE - DELETE /api/products/{id}?hard=true
     * Hard delete product (permanent deletion)
     * WARNING: Use only for data cleanup
     * Requires ADMIN role
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteHard(
            @PathVariable("id") Long id,
            @RequestParam(value = "hard", defaultValue = "false") boolean hard) {
        if (hard) {
            productService.deleteHard(id);
            return ResponseEntity.ok(
                    ApiResponse.success("Product permanently deleted")
            );
        } else {
            productService.delete(id);
            return ResponseEntity.ok(
                    ApiResponse.success("Product deleted successfully")
            );
        }
    }
}

