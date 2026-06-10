package com.shoes.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    private Long productId;
    private String productName;
    private String productImage;
    private String productSku;

    private Integer sizeVn;
    private Integer quantity;

    private Long unitPrice;
    private Long lineTotal;
}
