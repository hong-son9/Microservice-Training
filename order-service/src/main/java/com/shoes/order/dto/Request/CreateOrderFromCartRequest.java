package com.shoes.order.dto.Request;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderFromCartRequest {
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String note;

    private List<Long> selectedCartItemIds;
}