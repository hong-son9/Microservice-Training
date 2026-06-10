package com.shoes.order.repository;

import com.shoes.order.entity.Order;
import com.shoes.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderCode(String orderCode);
    @EntityGraph(attributePaths = {"items"})
    List<Order> findAll();

    @EntityGraph(attributePaths = {"items"})
    List<Order> findAllByBuyerUserId(Long id);

    @EntityGraph(attributePaths = {"items"})
    Optional<Order> findOrderById(Long orderId);
}
