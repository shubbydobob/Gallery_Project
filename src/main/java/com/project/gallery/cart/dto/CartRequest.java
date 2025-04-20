package com.project.gallery.cart.dto;

import com.project.gallery.cart.entity.Cart;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "장바구니 추가 요청 DTO")
public class CartRequest {

    @Schema(description = "상품 아이디", example = "1")
    private Integer itemId;

    @Schema(description = "장바구니")
    public Cart toEntity(Integer memberId) {
        return new Cart(memberId, itemId);
    }
}
