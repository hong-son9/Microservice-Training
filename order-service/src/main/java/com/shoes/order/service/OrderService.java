package com.shoes.order.service;

import com.shoes.order.dto.request.CreateOrderFromCartRequest;
import com.shoes.order.dto.request.CreateOrderRequest;
import com.shoes.order.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse create(CreateOrderRequest createOrderRequest);

    List<OrderResponse> getAllByUserId();

    List<OrderResponse> getAll();

    void cancelOrder(Long orderId);
    OrderResponse createFromCart(CreateOrderFromCartRequest createOrderFromCartRequest);
}
