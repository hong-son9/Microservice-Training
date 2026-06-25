package com.shoes.promotion.consumer;

import com.shoes.promotion.dto.event.OrderCancelledEvent;
import com.shoes.promotion.service.impl.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PromotionConsumer {
    public final PromotionService promotionService;
    @KafkaListener(topics = "order-cancelled-topic", groupId = "promotion-service-group", properties = {"spring.json.value.default.type=com.shoes.promotion.dto.event.OrderCancelledEvent"})
    public void orderCancelled(OrderCancelledEvent orderCancelledEvent) {
        try {
            if (orderCancelledEvent.getPromotionId() == null) {
                log.info("Đơn hàng {} không sử dụng mã khuyến mãi, không cần hoàn tác.", orderCancelledEvent.getOrderId());
                return;
            }
            if (orderCancelledEvent.getItems().isEmpty()) {
                log.warn("Đơn hàng {} không có sản phẩm nào, bỏ qua bước xử li khuyen mai!", orderCancelledEvent.getPromotionId());
                return;
            }
            promotionService.cancelPromotion(orderCancelledEvent.getPromotionId(), orderCancelledEvent.getBuyerUserId(),  orderCancelledEvent.getOrderId());
            log.info("Xu li khuyen mai {} hoàn tất thành công!", orderCancelledEvent.getPromotionId());

        } catch (Exception e) {
            log.error("Lỗi xảy ra trong quá trình xu li khuyen mai: ", e);
        }
    }
}
