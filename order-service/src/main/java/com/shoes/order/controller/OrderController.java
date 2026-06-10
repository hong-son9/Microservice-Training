package com.shoes.order.controller;

import com.shoes.order.dto.ApiResponse;
import com.shoes.order.dto.request.CreateOrderFromCartRequest;
import com.shoes.order.dto.request.CreateOrderRequest;
import com.shoes.order.dto.response.OrderResponse;
import com.shoes.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public OrderResponse create(@RequestBody CreateOrderRequest createOrderRequest) {
        return orderService.create(createOrderRequest);
    }
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<OrderResponse> getAll() {
        return orderService.getAll();
    }

    @GetMapping("/getOrderByUserId")
    public List<OrderResponse> getAllByUserId() {
        return orderService.getAllByUserId();
    }

    @PostMapping("/from-cart")
    public ResponseEntity<ApiResponse<OrderResponse>> createFromCart(
            @RequestBody CreateOrderFromCartRequest request) {

        OrderResponse response = orderService.createFromCart(request);
        return ResponseEntity.ok(
                ApiResponse.success(response, "Order ok")
        );
    }

    @PutMapping("/cancelOrder/{orderId}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok(
                ApiResponse.success("Cancel order ok")
        );
    }
}
