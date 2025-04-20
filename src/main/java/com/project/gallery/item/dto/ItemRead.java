package com.project.gallery.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "상품 조회 DTO")
public class ItemRead {

    @Schema(description = "상품 아이디", example = "1")
    private Integer id;

    @Schema(description = "상품 이름", example = "상품1")
    private String name;

    @Schema(description = "이미지 경로", example = "/img/001.jpg")
    private String imgPath;

    @Schema(description = "상품 가격", example = "10000")
    private Integer price;

    @Schema(description = "상품 할인률", example = "5")
    private Integer discountPer;
}
