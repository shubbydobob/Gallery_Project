package com.project.gallery.order.service;

import com.project.gallery.cart.service.CartService;
import com.project.gallery.item.dto.ItemRead;
import com.project.gallery.item.service.ItemService;
import com.project.gallery.order.dto.OrderRead;
import com.project.gallery.order.dto.OrderRequest;
import com.project.gallery.order.entity.Order;
import com.project.gallery.order.entity.OrderItem;
import com.project.gallery.order.repository.OrderItemRepository;
import com.project.gallery.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BaseOrderService implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final ItemService itemService;
    private final CartService cartService;

    // 주문 목록 조회
    @Override
    public List<OrderRead> findAll(Integer memberId) {

        //결괏값을 DTO로 변환 후 리턴
        return orderRepository.findAllByMemberIdOrderByIdDesc(memberId).stream().map(Order::toRead).toList();
    }

    // 주문 상세 조회
    @Override
    public OrderRead find(Integer id, Integer memberId) {
        Optional<Order> orderOptional = orderRepository.findByIdAndMemberId(id, memberId);

        if(orderOptional.isPresent()) {

            // 주문 조회 DTO로 변환
            OrderRead order = orderOptional.get().toRead();

            // 주문 상품 목록 조회
            List<OrderItem> orderItems = orderItemService.findAll(order.getId());

            // 주문 상품 목록의 상품 아이디를 추출
            List<Integer> orderItemIds = orderItems.stream().map(OrderItem::getItemId).toList();

            // 주문 상품 리스트의 상품 ID에 해당하는 상품 목록을 조회
            List<ItemRead> items = itemService.findAll(orderItemIds);

            // 응답 값에 상품 리스트 데이터를 설정
            order.setItems(items);

            return order;
        }
        return null;
    }

    // 주문 내용 저장
    @Override
    @Transactional
    public void order(OrderRequest orderRequest, Integer memberId) {

        // 주문 상품의 최종 결제 금액을 계산
        List<ItemRead> items = itemService.findAll(orderRequest.getItemIds());
        long amount = 0L;

        for (ItemRead item : items) {
            amount += item.getPrice() - item.getPrice().longValue() * item.getDiscountPer() / 100;
        }

        // 주문 요청에 최종 결제 금액 입력
        orderRequest.setAmount(amount);
        
        // 주문 저정
        Order order = orderRepository.save(orderRequest.toEntity(memberId));
        
        // 주문 상품 데이터 생성
        List<OrderItem> newOrderItems = new ArrayList<>();
        
        // 상품 아이디만큼 주문 상품 추가
        orderRequest.getItemIds().forEach((itemId) -> {
            OrderItem newOrderItem = new OrderItem(order.getId(), itemId);
            newOrderItems.add(newOrderItem);
        });
        
        // 주문 상품 데이터 저장
        orderItemService.saveAll(newOrderItems);
        
        // 장바구니 데이터 삭제(특정 회원)
        cartService.removeAll(order.getMemberId());
    }
}
