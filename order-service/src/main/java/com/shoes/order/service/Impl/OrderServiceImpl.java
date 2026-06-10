package com.shoes.order.service.Impl;

import com.shoes.order.client.CartClient;
import com.shoes.order.client.ProductClient;
import com.shoes.order.config.SecurityUtils;
import com.shoes.order.dto.ApiResponse;
import com.shoes.order.dto.event.OrderCancelledEvent;
import com.shoes.order.dto.request.CartItemsRequest;
import com.shoes.order.dto.request.CreateOrderFromCartRequest;
import com.shoes.order.dto.request.CreateOrderRequest;
import com.shoes.order.dto.request.OrderItemRequest;
import com.shoes.order.dto.response.*;
import com.shoes.order.entity.Order;
import com.shoes.order.entity.OrderItem;
import com.shoes.order.entity.OrderStatus;
import com.shoes.order.dto.event.OrderPlacedEvent;
import com.shoes.order.exception.ConflictException;
import com.shoes.order.exception.ResourceNotFoundException;
import com.shoes.order.repository.OrderRepository;
import com.shoes.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private ProductClient productClient;
    @Autowired
    private CartClient cartClient;
    @Autowired
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @CacheEvict(value = "orders_user", key = "#root.target.getCurrentUserId()")
    public OrderResponse create(CreateOrderRequest createOrderRequest) {

        for (int i = 0; i < createOrderRequest.getItems().size(); i++) {
            ResponseEntity<ApiResponse<ProductSizeResponse>> productSizeResponse =
                    productClient.getProductSize(createOrderRequest.getItems().get(i).getSizeVn(), createOrderRequest.getItems().get(i).getProductId());
            if (productSizeResponse.getBody() == null || !productSizeResponse.getBody().getSuccess()) {
                throw new ConflictException("There are no products in this size");
            }
            if (createOrderRequest.getItems().get(i).getQuantity() > productSizeResponse.getBody().getData().getQuantity()) {
                throw new ConflictException("The product is out of stock");
            }
        }
        List<CartItemsRequest> cartItemsRequests = createOrderRequest.getItems().stream()
                .map(item ->
                {
                    CartItemsRequest c = new CartItemsRequest();
                    c.setProductId(item.getProductId());
                    c.setQuantity(item.getQuantity());
                    return c;
                })
                .toList();
        ApiResponse<List<ProductSnapshotResponse>> apiResponse = productClient.getProducts(
                cartItemsRequests.stream()
                        .map(CartItemsRequest::getProductId)
                        .collect(Collectors.toList())
        );

        List<ProductSnapshotResponse> productSnapshotResponses = apiResponse.getData();
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
        Order savedOrder = orderRepository.save(order);
        List<OrderPlacedEvent.OrderItemEvent> itemEvents = savedOrder.getItems().stream()
                .map(i -> OrderPlacedEvent.OrderItemEvent.builder()
                        .productId(i.getProductId())
                        .quantity(i.getQuantity())
                        .sizeVn(i.getSizeVn())
                        .build()).toList();
        OrderPlacedEvent kafkaEvent = OrderPlacedEvent.builder()
                .orderId(savedOrder.getId())
                .orderCode(savedOrder.getOrderCode())
                .buyerUserId(savedOrder.getBuyerUserId())
                .items(itemEvents)
                .build();
        kafkaTemplate.send("order-placed-topic", kafkaEvent);
        return mapToResponse(savedOrder);

    }

    @Override
    @Cacheable(value = "orders_user", key = "#root.target.getCurrentUserId()")
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
    @Transactional
    @CacheEvict(value = "orders_user", key = "#root.target.getCurrentUserId()")
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOrderById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ConflictException("Orders can only be canceled when the status is PENDING.");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        List<OrderCancelledEvent.OrderItemCancelEvent> orderItemCancelEvents = order.getItems().stream().map(
                item -> OrderCancelledEvent.OrderItemCancelEvent.builder()
                        .sizeVn(item.getSizeVn())
                        .quantity(item.getQuantity())
                        .productId(item.getProductId())
                        .build()
        ).toList();
        OrderCancelledEvent orderCancelledEvent = OrderCancelledEvent.builder()
                .buyerUserId(order.getBuyerUserId())
                .orderId(orderId)
                .orderCode(order.getOrderCode())
                .items(orderItemCancelEvents).build();
        kafkaTemplate.send("order-cancelled-topic", orderCancelledEvent);
    }

    @Override
    public OrderResponse createFromCart(CreateOrderFromCartRequest createOrderFromCartRequest) {
        ResponseEntity<ApiResponse<CartResponse>> cartResponse = cartClient.getCartByUserId();
        if (cartResponse.getBody() == null || !cartResponse.getBody().getSuccess()) {
            throw new ConflictException("Failed to retrieve cart");
        }
        CartResponse cartData = cartResponse.getBody().getData();
        List<CartItemResponse> allCartItems = cartData.getItems();
        if (allCartItems == null || allCartItems.isEmpty()) {
            throw new ConflictException("CartItems is empty!");
        }
        List<CartItemResponse> selectedItems = allCartItems.stream()
                .filter(item -> createOrderFromCartRequest.getSelectedProductIds() != null &&
                        createOrderFromCartRequest.getSelectedProductIds().contains(item.getProductId()))
                .toList();

        if (selectedItems.isEmpty()) {
            throw new ConflictException("Not found any cart item");
        }
        Order order = Order.builder()
                .orderCode("SH-" + System.currentTimeMillis())
                .buyerUserId(SecurityUtils.getCurrentUserId())
                .receiverName(createOrderFromCartRequest.getReceiverName())
                .receiverPhone(createOrderFromCartRequest.getReceiverPhone())
                .receiverAddress(createOrderFromCartRequest.getReceiverAddress())
                .note(createOrderFromCartRequest.getNote())
                .status(OrderStatus.PENDING)
                .discountAmount(0L)
                .shippingFee(30000L)
                .build();
        long subtotal = 0;
        for (CartItemResponse cartItem : selectedItems) {
            long lineTotal = cartItem.getUnitPrice() * cartItem.getQuantity();
            subtotal += lineTotal;

            OrderItem orderItem = OrderItem.builder()
                    .productId(cartItem.getProductId())
                    .productName(cartItem.getProductName())
                    .productImage(cartItem.getProductImage())
                    .productSku(cartItem.getProductSlug())
                    .sizeVn(cartItem.getSizeVn())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(cartItem.getUnitPrice())
                    .lineTotal(lineTotal)
                    .build();
            order.addItem(orderItem);
        }
        order.setSubtotal(subtotal);
        order.setTotal(subtotal - order.getDiscountAmount() + order.getShippingFee());
        Order savedOrder = orderRepository.save(order);
        List<OrderPlacedEvent.OrderItemEvent> itemEvents = savedOrder.getItems().stream()
                .map(i -> OrderPlacedEvent.OrderItemEvent.builder()
                        .productId(i.getProductId())
                        .quantity(i.getQuantity())
                        .sizeVn(i.getSizeVn())
                        .build()).toList();
        OrderPlacedEvent kafkaEvent = OrderPlacedEvent.builder()
                .orderId(savedOrder.getId())
                .orderCode(savedOrder.getOrderCode())
                .buyerUserId(savedOrder.getBuyerUserId())
                .selectedProductIds(createOrderFromCartRequest.getSelectedProductIds())
                .items(itemEvents)
                .build();
        kafkaTemplate.send("order-placed-topic", kafkaEvent);
        return mapToResponse(savedOrder);
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

    public Long getCurrentUserId() {
        return SecurityUtils.getCurrentUserId();
    }
}
