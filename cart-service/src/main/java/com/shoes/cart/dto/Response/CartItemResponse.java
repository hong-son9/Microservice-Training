package com.shoes.cart.dto.Response;

import lombok.Data;

@Data
public class CartItemResponse {
    private Long id;
    private Long productId;
    private Integer sizeVn;
    private Integer quantity;
    private String productName;
    private String productSlug;
    private String productImage;
    private Long unitPrice;
    private long subTotal;
}
