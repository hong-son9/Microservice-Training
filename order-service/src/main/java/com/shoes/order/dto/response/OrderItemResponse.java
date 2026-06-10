package com.shoes.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long productId;
    private String productName;
    private String productImage;
    private String productSku;

    private Integer sizeVn;
    private Integer quantity;

    private Long unitPrice;
    private Long lineTotal;
}
