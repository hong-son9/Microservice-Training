package com.shoes.cart.consumer;

import com.shoes.cart.dto.Response.CartResponse;
import com.shoes.cart.dto.event.OrderPlacedEvent;
import com.shoes.cart.entity.Cart;
import com.shoes.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CartConsumer {

    private final CartService cartService;

    @KafkaListener(topics = "order-placed-topic", groupId = "cart-service-group")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("Kafka dọn các sản phẩm theo Product ID cho User: {}", event.getBuyerUserId());
        try {
            CartResponse cart = cartService.getCartByUserId(event.getBuyerUserId());
            boolean hasAnyMatch = cart.getItems().stream()
                    .anyMatch(item -> event.getSelectedProductIds().contains(item.getProductId()));
            if (hasAnyMatch) {
                cartService.removeProductsFromCart(event.getBuyerUserId(), event.getSelectedProductIds());
                log.info("Dọn dẹp giỏ hàng theo Product ID hoàn tất!");
            } else {
                log.warn("Không tìm thấy sản phẩm nào trùng khớp trong giỏ hàng của User {}, bỏ qua bước xóa!", event.getBuyerUserId());
            }
        } catch (Exception e) {
            log.error("Lỗi khi xóa giỏ hàng ngầm: ", e);
        }
    }
}