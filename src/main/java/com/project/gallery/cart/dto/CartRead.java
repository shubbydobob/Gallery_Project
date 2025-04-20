package com.project.gallery.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "장바구니 조회 DTO")
public class CartRead {

    @Schema(description = "장바구니 아이디", example = "1")
    private Integer id;

    @Schema(description = "상품 아이디", example = "1")
    private Integer itemId;
}
