package com.shoes.product.consumer;

import com.shoes.product.dto.event.OrderCancelledEvent;
import com.shoes.product.dto.event.OrderPlacedEvent;
import com.shoes.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductConsumer {
    private final ProductService productService;
    @KafkaListener(topics = "order-placed-topic", groupId = "product-service-group")
    @CacheEvict(value = "products", key = "'all'")
    public void orderPlaced(OrderPlacedEvent orderPlacedEvent) {
        try {
            if (orderPlacedEvent.getItems().isEmpty()) {
                log.warn("Đơn hàng {} không có sản phẩm nào, bỏ qua bước trừ kho!", orderPlacedEvent.getOrderCode());
                return;
            }
            productService.deductStock(orderPlacedEvent.getItems());
            log.info("Khấu trừ kho cho đơn hàng {} hoàn tất thành công!", orderPlacedEvent.getOrderCode());

        } catch (Exception e) {
            log.error("Lỗi xảy ra trong quá trình trừ kho ngầm: ", e);
        }
    }

    @KafkaListener(topics = "order-cancelled-topic", groupId = "produce-service-group", properties = {"spring.json.value.default.type=com.shoes.product.dto.event.OrderCancelledEvent"})
    @CacheEvict(value = "products", key = "'all'")
    public void orderCancelled(OrderCancelledEvent orderCancelledEvent) {
        try {
            if (orderCancelledEvent.getItems().isEmpty()) {
                log.warn("Đơn hàng {} không có sản phẩm nào, bỏ qua bước cộng kho!", orderCancelledEvent.getOrderCode());
                return;
            }
            productService.refundStock(orderCancelledEvent.getItems());
            log.info("Cộng kho cho đơn hàng {} hoàn tất thành công!", orderCancelledEvent.getOrderCode());

        } catch (Exception e) {
            log.error("Lỗi xảy ra trong quá trình cộng kho ngầm: ", e);
        }
    }

}
