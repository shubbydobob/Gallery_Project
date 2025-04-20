package com.project.gallery.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원가입 요청 DTO")
public class AccountJoinRequest {

    @Schema(description = "회원 이름", example = "홍길동")
    private String name;

    @Schema(description = "회원 아이디", example = "test1234")
    private String loginId;

    @Schema(description = "회원 비밀번호", example = "password1234")
    private String loginPw;
}
