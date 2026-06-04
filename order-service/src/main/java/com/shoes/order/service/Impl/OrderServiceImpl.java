package com.shoes.order.service.Impl;

import com.shoes.order.client.ProductClient;
import com.shoes.order.config.SecurityUtils;
import com.shoes.order.dto.Request.CartItemsRequest;
import com.shoes.order.dto.Request.CreateOrderRequest;
import com.shoes.order.dto.Request.OrderItemRequest;
import com.shoes.order.dto.Response.OrderItemResponse;
import com.shoes.order.dto.Response.OrderResponse;
import com.shoes.order.dto.Response.ProductSnapshotResponse;
import com.shoes.order.entity.Order;
import com.shoes.order.entity.OrderItem;
import com.shoes.order.entity.OrderStatus;
import com.shoes.order.repository.OrderRepository;
import com.shoes.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private ProductClient productClient;

    @Override
    public OrderResponse create(CreateOrderRequest createOrderRequest) {
//       userClient.getUser(SecurityUtils.getCurrentUserId());
        List<CartItemsRequest> cartItemsRequests = createOrderRequest.getItems().stream()
                .map(item ->
                {
                    CartItemsRequest c = new CartItemsRequest();
                    c.setProductId(item.getProductId());
                    c.setQuantity(item.getQuantity());
                    return c;
                })
                .toList();
        List<ProductSnapshotResponse> productSnapshotResponses = productClient.getProducts(
                cartItemsRequests.stream()
                        .map(CartItemsRequest::getProductId)
                        .collect(Collectors.toList()));
        Order order = Order.builder()
                .orderCode(generateOrderCode())
                .buyerUserId(SecurityUtils.getCurrentUserId())
                .receiverName(createOrderRequest.getReceiverName())
                .receiverPhone(createOrderRequest.getReceiverPhone())
                .receiverAddress(createOrderRequest.getReceiverAddress())
                .note(createOrderRequest.getNote())
                .status(OrderStatus.PENDING)
                .discountAmount(0L)
                .shippingFee(30000L)
                .build();
        long subtotal = 0;
        for (int i = 0; i < createOrderRequest.getItems().size(); i++) {
            OrderItemRequest orderItemRequest = createOrderRequest.getItems().get(i);
            ProductSnapshotResponse productSnapshotResponse = productSnapshotResponses.get(i);
            long lineTotal = productSnapshotResponse.getPrice() * orderItemRequest.getQuantity();
            subtotal += lineTotal;

            OrderItem item = OrderItem.builder()
                    .productId(productSnapshotResponse.getProductId())
                    .productName(productSnapshotResponse.getName())
                    .productImage(productSnapshotResponse.getImage())
                    .productSku(productSnapshotResponse.getSku())
                    .sizeVn(orderItemRequest.getSizeVn())
                    .quantity(orderItemRequest.getQuantity())
                    .unitPrice(productSnapshotResponse.getPrice())
                    .lineTotal(lineTotal)
                    .build();

            order.addItem(item);
        }
        order.setSubtotal(subtotal);
        order.setTotal(subtotal - order.getDiscountAmount() + order.getShippingFee());
        return mapToResponse(orderRepository.save(order));

    }

    @Override
    public List<OrderResponse> getAllByUserId() {
        List<Order> orders = orderRepository.findAllByBuyerUserId(SecurityUtils.getCurrentUserId());
        return orders.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private String generateOrderCode() {
        return "SH-" + System.currentTimeMillis();
    }

    @Override
    public List<OrderResponse> getAll() {

        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public void cancel(Long id) {

    }
    private OrderResponse mapToResponse(Order order) {

        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .buyerUserId(order.getBuyerUserId())
                .receiverName(order.getReceiverName())
                .receiverPhone(order.getReceiverPhone())
                .receiverAddress(order.getReceiverAddress())
                .subtotal(order.getSubtotal())
                .discountAmount(order.getDiscountAmount())
                .shippingFee(order.getShippingFee())
                .total(order.getTotal())
                .status(order.getStatus())
                .items(
                        order.getItems().stream()
                                .map(i -> OrderItemResponse.builder()
                                        .productId(i.getProductId())
                                        .productName(i.getProductName())
                                        .productImage(i.getProductImage())
                                        .productSku(i.getProductSku())
                                        .sizeVn(i.getSizeVn())
                                        .quantity(i.getQuantity())
                                        .unitPrice(i.getUnitPrice())
                                        .lineTotal(i.getLineTotal())
                                        .build())
                                .toList()
                )
                .build();
    }
}
