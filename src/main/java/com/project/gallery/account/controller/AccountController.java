package com.project.gallery.account.controller;

import com.project.gallery.account.dto.AccountJoinRequest;
import com.project.gallery.account.dto.AccountLoginRequest;
import com.project.gallery.account.etc.AccountConstants;
import com.project.gallery.account.helper.AccountHelper;
import com.project.gallery.block.service.BlockService;
import com.project.gallery.common.util.HttpUtils;
import com.project.gallery.common.util.TokenUtils;
import com.project.gallery.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
@Tag(name = "Account", description = "승인(회원가입, 로그인, 토큰 관련)")
public class AccountController {

    private final AccountHelper accountHelper;
    private final BlockService blockService;
    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원가입 요청")
    @PostMapping("/api/account/join")
    public ResponseEntity<?> join(@RequestBody AccountJoinRequest joinReq) {
        if (!StringUtils.hasLength(joinReq.getName()) ||
                !StringUtils.hasLength(joinReq.getLoginId()) || !StringUtils.hasLength(joinReq.getLoginPw())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (memberService.find(joinReq.getLoginId()) != null) {
            return new ResponseEntity<>("이미 존재하는 아이디입니다.", HttpStatus.CONFLICT);
        }

        accountHelper.join(joinReq);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "로그인", description = "로그인 요청")
    @PostMapping("/api/account/login")
    public ResponseEntity<?> login(HttpServletRequest req, HttpServletResponse res, @RequestBody AccountLoginRequest loginReq) {
        if (!StringUtils.hasLength(loginReq.getLoginId()) || !StringUtils.hasLength(loginReq.getLoginPw())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String output = accountHelper.login(loginReq, req, res);

        if (output == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @Operation(summary = "유효성 체크")
    @GetMapping("/api/account/check")
    public ResponseEntity<?> check(HttpServletRequest req) {
        return new ResponseEntity<>(accountHelper.isLoggedIn(req), HttpStatus.OK);
    }

    @Operation(summary = "로그아웃", description = "로그아웃 요청")
    @GetMapping("/api/account/logout")
    public ResponseEntity<?> logout(HttpServletRequest req, HttpServletResponse res) {
        accountHelper.logout(req, res);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "토큰 재발급", description = "토큰 유효 기간 확인 후 액세트 토큰 재발급")
    @GetMapping("/api/account/token")
    public ResponseEntity<?> regenerate(HttpServletRequest request) {
        String refreshToken = HttpUtils.getCookieValue(request, AccountConstants.REFRESH_TOKEN_NAME);

        // 리프레시 토큰이 없거나, 유효하지 않거나, 차단된 경우 401 반환
        if (!StringUtils.hasLength(refreshToken) ||
                !TokenUtils.isValid(refreshToken) ||
                blockService.has(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않거나 존재하지 않습니다.");
        }

        Map<String, Object> tokenBody = TokenUtils.getBody(refreshToken);
        Integer memberId = (Integer) tokenBody.get(AccountConstants.MEMBER_ID_NAME);

        String accessToken = TokenUtils.generate(AccountConstants.ACCESS_TOKEN_NAME,
                AccountConstants.MEMBER_ID_NAME,
                memberId,
                AccountConstants.ACCESS_TOKEN_EXP_MINUTES);

        return ResponseEntity.ok(accessToken);
    }

}
