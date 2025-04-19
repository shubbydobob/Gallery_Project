package com.project.gallery.order.service;

import com.project.gallery.common.util.EncryptionUtils;
import com.project.gallery.order.dto.OrderRequest;
import com.project.gallery.order.entity.Order;
import com.project.gallery.order.entity.OrderItem;
import com.project.gallery.order.repository.OrderItemRepository;
import com.project.gallery.order.repository.OrderRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseOrderItemService implements OrderItemService {
    
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    // 주문 목록 조회
    @Override
    public List<OrderItem> findAll(Integer orderId) {
        return orderItemRepository.findAllByOrderId(orderId);
    }
    
    // 주문 상품 데이터 저장
    @Override
    public void saveAll(List<OrderItem> orderItems) {
        orderItemRepository.saveAll(orderItems);
    }
}
