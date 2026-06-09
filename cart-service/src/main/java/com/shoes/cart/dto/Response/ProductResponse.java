package com.shoes.cart.dto.Response;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String slug;
    private String image;
    private Long price;
}