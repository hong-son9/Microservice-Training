package com.shoes.product.service.Impl;

import com.shoes.product.dto.Request.CreateProductRequest;
import com.shoes.product.dto.Request.UpdateProductRequest;
import com.shoes.product.dto.Response.ProductResponse;
import com.shoes.product.dto.event.OrderCancelledEvent;
import com.shoes.product.dto.event.OrderPlacedEvent;
import com.shoes.product.entity.*;
import com.shoes.product.exception.ConflictException;
import com.shoes.product.exception.ResourceNotFoundException;
import com.shoes.product.mapper.ProductMapper;
import com.shoes.product.repository.BrandRepository;
import com.shoes.product.repository.CategoryRepository;
import com.shoes.product.repository.ProductRepository;
import com.shoes.product.repository.ProductSizeRepository;
import com.shoes.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for Product management
 * Handles complex product operations including sizes, categories, images
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSizeRepository productSizeRepository;

    /**
     * Create a new product with sizes and categories
     * Validates unique constraints (SKU, slug, name)
     */
    @Override
    public ProductResponse create(CreateProductRequest request) {
        // Validate unique SKU
        if (productRepository.existsBySku(request.getSku())) {
            throw new ConflictException("Product", "sku", request.getSku());
        }

        // Validate unique name
        if (productRepository.existsByName(request.getName())) {
            throw new ConflictException("Product", "name", request.getName());
        }

        // Validate unique slug
        if (productRepository.existsBySlug(request.getSlug())) {
            throw new ConflictException("Product", "slug", request.getSlug());
        }

        // Fetch brand
        Brand brand = brandRepository.findByName(request.getBrandName())
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "name", request.getBrandName()));

        // Fetch and set categories
        Set<Category> categories = new HashSet<>();
        if (request.getCategory() != null && !request.getCategory().isEmpty()) {
            for (String categoryName : request.getCategory()) {
                Category category = categoryRepository.findByName(categoryName)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "name", categoryName));
                categories.add(category);
            }
        }

        // Create product entity
        Product product = productMapper.toEntity(request);
        product.setBrand(brand);
        product.setCategories(categories);

        // Add sizes to product
        if (request.getSizes() != null && !request.getSizes().isEmpty()) {
            for (var sizeRequest : request.getSizes()) {
                ProductSize size = new ProductSize();
                size.setSizeVn(sizeRequest.getSizeVn());
                size.setQuantity(sizeRequest.getQuantity());
                product.addSize(size);
            }
        }

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    /**
     * Get product by ID with all related data
     */
    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        
        // Increment view count (optional - can be done asynchronously in production)
        return productMapper.toResponse(product);
    }

    /**
     * Get all products (non-deleted)
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toResponse)
                .toList();
    }

    /**
     * Get products by list of IDs (for snapshots in order service)
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllById(List<Long> ids) {
        List<Product> products = productRepository.findByIdIn(ids);
        return products.stream()
                .map(productMapper::toResponse)
                .toList();
    }

    /**
     * Get only active products
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllActive() {
        List<Product> products = productRepository.findByStatusAndIsDeletedFalse(ProductStatus.ACTIVE);
        return products.stream()
                .map(productMapper::toResponse)
                .toList();
    }

    /**
     * Update existing product
     * Handles brand and category updates
     */
    @Override
    @Transactional
    public ProductResponse update(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        // Validate unique constraints if SKU/slug/name are being updated
        if (request.getSku() != null && !request.getSku().equals(product.getSku())) {
            if (productRepository.existsBySku(request.getSku())) {
                throw new ConflictException("Product", "sku", request.getSku());
            }
        }

        if (request.getSlug() != null && !request.getSlug().equals(product.getSlug())) {
            if (productRepository.existsBySlug(request.getSlug())) {
                throw new ConflictException("Product", "slug", request.getSlug());
            }
        }

        if (request.getName() != null && !request.getName().equals(product.getName())) {
            if (productRepository.existsByName(request.getName())) {
                throw new ConflictException("Product", "name", request.getName());
            }
        }

        // Update brand if provided
        if (request.getBrandName() != null) {
            Brand brand = brandRepository.findByName(request.getBrandName())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", "name", request.getBrandName()));
            product.setBrand(brand);
        }

        // Update categories if provided
        if (request.getCategory() != null && !request.getCategory().isEmpty()) {
            Set<Category> categories = new HashSet<>();
            for (String categoryName : request.getCategory()) {
                Category category = categoryRepository.findByName(categoryName)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "name", categoryName));
                categories.add(category);
            }
            product.setCategories(categories);
        }

        // Update product status if provided
        if (request.getStatus() != null) {
            try {
                product.setStatus(ProductStatus.valueOf(request.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid product status: " + request.getStatus());
            }
        }

        for (var sizeRequest : request.getSizes()) {
            Optional<ProductSize> productSizeOpt = productSizeRepository
                    .findByProductIdAndSizeVn(id, sizeRequest.getSizeVn());

            if (productSizeOpt.isPresent()) {
                ProductSize productSize = productSizeOpt.get();
                int newQuantity = productSize.getQuantity() + sizeRequest.getQuantity();
                productSize.setQuantity(newQuantity);
                productSizeRepository.save(productSize);
            } else {
                throw new ConflictException("Product size not found");
            }
        }

        // Use mapper to update base fields (non-nested)
        productMapper.updateEntityFromRequest(request, product);

        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    /**
     * Soft delete product
     */
    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        product.softDelete();
        productRepository.save(product);
    }

    /**
     * Hard delete product
     * WARNING: Use only for data cleanup, not for production
     */
    @Override
    public void deleteHard(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(product);
    }

    @Override
    @Transactional
    public void deductStock(List<OrderPlacedEvent.OrderItemEvent> itemEvents) {
        for (OrderPlacedEvent.OrderItemEvent itemEvent : itemEvents) {
            ProductSize productSize = productSizeRepository
                    .findByProductIdAndSizeVn(itemEvent.getProductId(), itemEvent.getSizeVn())
                    .orElseThrow(() -> new ConflictException(
                            "Product ID " + itemEvent.getProductId() + " with Size " + itemEvent.getSizeVn() + " does not exist."
                    ));
            if (itemEvent.getQuantity() > productSize.getQuantity()) {
                throw new ConflictException("The product with Product ID " + itemEvent.getProductId() + " and Size " + itemEvent.getSizeVn() + " is out of stock.");
            }
            int newStock = productSize.getQuantity() - itemEvent.getQuantity();
            productSize.setQuantity(newStock);
            productSizeRepository.save(productSize);
        }
    }

    @Override
    public void refundStock(List<OrderCancelledEvent.OrderItemCancelEvent> itemCancelEvents) {
        for (OrderCancelledEvent.OrderItemCancelEvent itemCancelEvent : itemCancelEvents) {
            ProductSize productSize = productSizeRepository
                    .findByProductIdAndSizeVn(itemCancelEvent.getProductId(), itemCancelEvent.getSizeVn())
                    .orElseThrow(() -> new ConflictException(
                            "Product ID " + itemCancelEvent.getProductId() + " with Size " + itemCancelEvent.getSizeVn() + " does not exist."
                    ));
            int newStock = productSize.getQuantity() + itemCancelEvent.getQuantity();
            productSize.setQuantity(newStock);
            productSizeRepository.save(productSize);
        }
    }
}
