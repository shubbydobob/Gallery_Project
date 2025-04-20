package com.project.gallery.order.dto;

import com.project.gallery.item.dto.ItemRead;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Schema(description = "주문 조회 DTO")
public class OrderRead {

    @Schema(description = "주문 아이디", example = "1")
    private Integer id;

    @Schema(description = "주문자 이름", example = "홍길동")
    private String name;

    @Schema(description = "주문자 주소", example = "서울시 강남구")
    private String address;

    @Schema(description = "주문자 결제 수단", example = "신용카드")
    private String payment;

    @Schema(description = "주문 수량", example = "1")
    private Long amount;

    @Schema(description = "주문 생성일", example = "2023-10-01T12:00:00")
    private LocalDateTime created;

    @Schema(description = "상품 아이디 목록", example = "[1, 2, 3]")
    private List<ItemRead> items;
}
