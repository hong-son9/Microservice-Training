package com.shoes.cart.consumer;

import com.shoes.cart.dto.event.OrderPlacedEvent;
import com.shoes.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor // Tự động Inject CartService qua Constructor (Lombok)
public class CartConsumer {

    private final CartService cartService;

    @KafkaListener(topics = "order-placed-topic", groupId = "cart-service-group")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("Kafka dọn các sản phẩm theo Product ID cho User: {}", event.getBuyerUserId());
        try {
            cartService.removeProductsFromCart(event.getSelectedProductIds());
            log.info("Dọn dẹp giỏ hàng theo Product ID hoàn tất!");
        } catch (Exception e) {
            log.error("Lỗi khi xóa giỏ hàng ngầm: ", e);
        }
    }
}