package com.project.gallery.order.dto;

import com.project.gallery.order.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "주문 요청 DTO")
public class OrderRequest {

    @Schema(description = "회원 아이디", example = "홍길동")
    private String name;

    @Schema(description = "배송지", example = "서울특별시 강남구")
    private String address;

    @Schema(description = "결제 수단", example = "신용카드")
    private String payment;

    @Schema(description = "카드 번호", example = "1234-5678-9012-3456")
    private String cardNumber;

    @Schema(description = "주문 수량", example = "1")
    private Long amount;

    @Schema(description = "상품 아이디 목록", example = "[1, 2, 3]")
    private List<Integer> itemIds;

    // 엔티티 객체로 변환
    public Order toEntity(Integer memberId) {
        return new Order(memberId, name, address, payment, cardNumber, amount);
    }
}
