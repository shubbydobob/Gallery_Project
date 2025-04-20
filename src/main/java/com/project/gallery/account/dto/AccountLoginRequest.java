package com.project.gallery.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "로그인 요청 DTO")
public class AccountLoginRequest {

    @Schema(description = "회원 아이디", example = "test1234")
    private String loginId;

    @Schema(description = "회원 비밀번호", example = "password1234")
    private String loginPw;
}
