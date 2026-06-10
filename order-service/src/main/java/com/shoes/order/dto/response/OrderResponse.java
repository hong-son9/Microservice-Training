package com.shoes.order.dto.response;

import com.shoes.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private String orderCode;

    private Long buyerUserId;

    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;

    private Long subtotal;
    private Long discountAmount;
    private Long shippingFee;
    private Long total;

    private OrderStatus status;

    private List<OrderItemResponse> items;
}
