package com.shoes.product.service.Impl;

import com.shoes.product.dto.Request.CreateProductRequest;
import com.shoes.product.dto.Response.ProductResponse;
import com.shoes.product.dto.Response.ProductSizeResponse;
import com.shoes.product.entity.*;
import com.shoes.product.repository.BrandRepository;
import com.shoes.product.repository.CategoryRepository;
import com.shoes.product.repository.ProductRepository;
import com.shoes.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public ProductResponse create(CreateProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new RuntimeException("SKU already exists");
        }
        if (productRepository.existsByName(request.getName())) {
            throw new RuntimeException("Product name already exists");
        }
        Brand brand = brandRepository.findByName(request.getBrandName()).orElseThrow(() -> new RuntimeException("Brand not found"));
        Set<Category> categories = new HashSet<>();
        if (request.getCategory() != null) {
            categories = request.getCategory()
                    .stream()
                    .map(name -> categoryRepository.findByName(name).orElseThrow(() -> new RuntimeException("Category not found")))
                    .collect(Collectors.toSet());
        }

        Product product = Product.builder()
                .name(request.getName())
                .sku(request.getSku())
                .slug(request.getSlug())
                .price(request.getPrice())
                .description(request.getDescription())
                .brand(brand)
                .categories(categories)
                .salePrice(request.getSalePrice())
                .importPrice(request.getImportPrice())
                .status(ProductStatus.ACTIVE)
                .build();
        if (request.getSizes() != null) {
            for (ProductSize size : request.getSizes()) {
                size.setProduct(product);
                product.getSizes().add(size);
            }
        }
        return toProductResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return toProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::toProductResponse).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getAllById(List<Long> ids) {
        List<Product> products = productRepository.findByIdIn(ids);
        return products.stream()
                .map(this::toProductResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {

    }
    private ProductResponse toProductResponse(Product product) {
        List<ProductSizeResponse> sizeResponses = product.getSizes().stream()
                .map(size -> ProductSizeResponse.builder()
                        .id(size.getId())
                        .sizeVn(size.getSizeVn())
                        .quantity(size.getQuantity())
                        .build())
                .toList();

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .slug(product.getSlug())
                .price(product.getPrice())
                .description(product.getDescription())
                .brandName(Optional.ofNullable(product.getBrand()).map(Brand::getName).orElse(null))
                .category(product.getCategories().stream().map(Category::getName).collect(Collectors.toSet()))
                .sizes(sizeResponses)
                .salePrice(product.getSalePrice())
                .importPrice(product.getImportPrice())
                .status(product.getStatus())
                .build();
    }
}
