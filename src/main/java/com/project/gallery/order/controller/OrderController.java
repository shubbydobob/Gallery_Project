package com.project.gallery.order.controller;

import com.project.gallery.account.helper.AccountHelper;
import com.project.gallery.order.dto.OrderRead;
import com.project.gallery.order.dto.OrderRequest;
import com.project.gallery.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = " 주문", description = "주문 기능")
public class OrderController {

    private final AccountHelper accountHelper;
    private final OrderService orderService;


    @Operation(summary = "주문", description = "주문 목록")
    @GetMapping("/api/orders")
    public ResponseEntity<?> readAll(HttpServletRequest request) {

        // 로그인 회원 아이디
        Integer memberId = accountHelper.getMemberId(request);

        // 주문 목록
        List<OrderRead> orders = orderService.findAll(memberId);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Operation(summary = "주문", description = "주문 내역 조회")
    @GetMapping("/api/orders/{id}")
    public ResponseEntity<?> read(HttpServletRequest request, @PathVariable Integer id) {

        // 로그인 회원 아이디
        Integer memberId = accountHelper.getMemberId(request);

        // 주문 조회
        OrderRead order = orderService.find(id,memberId);

        if(order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @Operation(summary = "주문", description = "주문 하기")
    @PostMapping("/api/orders")
    public ResponseEntity<?> add(HttpServletRequest request, @RequestBody OrderRequest orderRequest) {

        // 로그인 회원 아이디
        Integer memberId = accountHelper.getMemberId(request);

        // 주문 입력
        orderService.order(orderRequest, memberId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
