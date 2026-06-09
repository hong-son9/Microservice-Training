package com.shoes.order.service;

import com.shoes.order.dto.Request.CreateOrderFromCartRequest;
import com.shoes.order.dto.Request.CreateOrderRequest;
import com.shoes.order.dto.Response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse create(CreateOrderRequest createOrderRequest);

    List<OrderResponse> getAllByUserId();

    List<OrderResponse> getAll();

    void cancel(Long id);
    OrderResponse createFromCart(CreateOrderFromCartRequest createOrderFromCartRequest);
}
