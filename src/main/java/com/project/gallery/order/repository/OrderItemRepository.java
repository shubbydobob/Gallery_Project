package com.project.gallery.order.repository;

import com.project.gallery.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    // 주문 상품 목록 조회
    List<OrderItem> findAllByOrderId(Integer orderId);
}
