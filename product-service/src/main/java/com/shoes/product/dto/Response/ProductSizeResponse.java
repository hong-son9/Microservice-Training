package com.shoes.product.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSizeResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Integer sizeVn;
    private Integer quantity;
}