package com.project.gallery.account.controller;

import com.project.gallery.account.dto.AccountJoinRequest;
import com.project.gallery.account.dto.AccountLoginRequest;
import com.project.gallery.account.etc.AccountConstants;
import com.project.gallery.account.helper.AccountHelper;
import com.project.gallery.block.service.BlockService;
import com.project.gallery.common.util.HttpUtils;
import com.project.gallery.common.util.TokenUtils;
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
public class AccountController {

    private final AccountHelper accountHelper;
    private final BlockService blockService;

    @PostMapping("/api/account/join")
    public ResponseEntity<?> join(@RequestBody AccountJoinRequest joinReq) {
        if(!StringUtils.hasLength(joinReq.getName()) ||
                !StringUtils.hasLength(joinReq.getLoginId()) || !StringUtils.hasLength(joinReq.getLoginPw())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        accountHelper.join(joinReq);
        return new  ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/api/account/login")
    public ResponseEntity<?> login(HttpServletRequest req, HttpServletResponse res, @RequestBody AccountLoginRequest loginReq) {
        if(!StringUtils.hasLength(loginReq.getLoginId()) || !StringUtils.hasLength(loginReq.getLoginPw())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String output = accountHelper.login(loginReq, req, res);

        if(output == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/api/account/check")
    public ResponseEntity<?> check(HttpServletRequest req) {
        return new ResponseEntity<>(accountHelper.isLoggedIn(req), HttpStatus.OK);
    }

    @GetMapping("/api/account/logout")
    public ResponseEntity<?> logout(HttpServletRequest req, HttpServletResponse res) {
        accountHelper.logout(req, res);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/api/account/token")
    public ResponseEntity<?> regenerate(HttpServletRequest request) {
        String refreshToken = HttpUtils.getCookieValue(request, AccountConstants.REFRESH_TOKEN_NAME);

        // 리프레시 토큰이 없거나, 유효하지 않거나, 차단된 경우 401 반환
        if (!StringUtils.hasLength(refreshToken) ||
                !TokenUtils.isTokenValid(refreshToken) ||
                blockService.isTokenBlocked(refreshToken)) {
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
