package com.project.gallery.cart.controller;

import com.project.gallery.account.helper.AccountHelper;
import com.project.gallery.cart.dto.CartRead;
import com.project.gallery.cart.dto.CartRequest;
import com.project.gallery.cart.entity.Cart;
import com.project.gallery.cart.repository.CartRepository;
import com.project.gallery.cart.service.CartService;
import com.project.gallery.item.dto.ItemRead;
import com.project.gallery.item.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ItemService itemService;
    private final AccountHelper accountHelper;

    @GetMapping("/api/cart/items")
    public ResponseEntity<?> readAll(HttpServletRequest request) {

        // 로그인 회원 아이디
        Integer memberId = accountHelper.getMemberId(request);

        // 장바구니 목록 조회
        List<CartRead> carts = cartService.findAll(memberId);

        // 장바구니 안에 있는 상품 아이디로 상품을 조회
        List<Integer> itemIds = carts.stream().map(CartRead::getItemId).toList();
        List<ItemRead> items = itemService.findAll(itemIds);

        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    
    @PostMapping("/api/carts")
    public ResponseEntity<?> push(HttpServletRequest request, @RequestBody CartRequest cartRequest) {
        
        // 로그인 회원 아이디
        Integer memberId = accountHelper.getMemberId(request);
        
        // 장바구니 데이터 조회(특정 상품)
        CartRead cart = cartService.find(memberId, cartRequest.getItemId());
        
        // 장바구니 데이터가 없다면
        if (cart == null) {
            cartService.save(cartRequest.toEntity(memberId));
        }
        
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
    
    @DeleteMapping("/api/cart/items/{itemId}")
    public ResponseEntity<?> remove(HttpServletRequest request, @PathVariable("itemId") Integer itemId) {
        
        // 로그인 회원 아이디
        Integer memberId = accountHelper.getMemberId(request);
        
        cartService.remove(memberId, itemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
