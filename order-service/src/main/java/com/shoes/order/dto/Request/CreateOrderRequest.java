package com.shoes.order.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String note;

    private Long promotionId;

    private List<OrderItemRequest> items;
}
