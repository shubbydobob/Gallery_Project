package com.project.gallery.account.helper;

import com.project.gallery.account.dto.AccountJoinRequest;
import com.project.gallery.account.dto.AccountLoginRequest;
import com.project.gallery.account.etc.AccountConstants;
import com.project.gallery.block.service.BlockService;
import com.project.gallery.common.util.HttpUtils;
import com.project.gallery.common.util.TokenUtils;
import com.project.gallery.member.entity.Member;
import com.project.gallery.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Primary
@RequiredArgsConstructor
public class TokenAccountHelper implements AccountHelper {

    private final MemberService memberService;
    private final BlockService blockService;

    // 엑세스 토큰 조회
    private String getAccessToken(HttpServletRequest request) {
        return HttpUtils.getBearerToken(request);
    }

    // 리프레시 토큰 조회
    private String getRefreshToken(HttpServletRequest request) {
        return HttpUtils.getCookieValue(request, AccountConstants.REFRESH_TOKEN_NAME);
    }

    // 회원 아이디 조회
    private Integer getMemberId(String token) {
        if (TokenUtils.isTokenValid(token)) {
            Map<String, Object> tokenBody = TokenUtils.getBody(token);
            return (Integer) tokenBody.get(AccountConstants.MEMBER_ID_NAME);
        }
        return null;
    }

    // 회원가입
    @Override
    public void join(AccountJoinRequest joinRequest) {
        // 회원가입 요청을 처리하는 로직
        memberService.save(joinRequest.getName(), joinRequest.getLoginId(), joinRequest.getLoginPw());
    }

    // 로그인
    @Override
    public String login(AccountLoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        Member member = memberService.find(loginRequest.getLoginId(), loginRequest.getLoginPw());

        if (member == null) {
            return null;
        }

        Integer memberId = member.getId();

        // 엑세스 토큰 생성
        String accessToken = TokenUtils.generate(AccountConstants.ACCESS_TOKEN_NAME, AccountConstants.MEMBER_ID_NAME,
                memberId, AccountConstants.ACCESS_TOKEN_EXP_MINUTES);

        // 리프레시 토큰 생성
        String refreshToken = TokenUtils.generate(AccountConstants.REFRESH_TOKEN_NAME, AccountConstants.MEMBER_ID_NAME,
                memberId, AccountConstants.REFRESH_TOKEN_EXP_MINUTES);

        // 리프레시 토큰을 쿠키에 저장
        HttpUtils.setCookie(response, AccountConstants.REFRESH_TOKEN_NAME, refreshToken, 0);

        return accessToken;
    }

    // 회원 아이디 조회
    @Override
    public Integer getMemberId(HttpServletRequest request) {
        return this.getMemberId(getAccessToken(request));
    }

    // 로그인 여부 확인
    @Override
    public boolean isLoggedIn(HttpServletRequest request) {

        if (TokenUtils.isTokenValid(getAccessToken(request))) {
            return true;
        }

        // 리프레시 토큰 조회
        String refreshToken = getRefreshToken(request);

        // 리프레시 토큰의 유효성 확인
        return TokenUtils.isTokenValid(refreshToken) && !blockService.isTokenBlocked(refreshToken);
    }

    // 로그아웃
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        // 리프레시 토큰 조회
        String refreshToken = getRefreshToken(request);

        if (refreshToken != null) {
            // 리프레시 토큰 삭제
            HttpUtils.removeCookie(response, AccountConstants.REFRESH_TOKEN_NAME);

            if (!blockService.isTokenBlocked(refreshToken)) {
                // 블록된 토큰 저장
                blockService.saveBlockToken(refreshToken);
            }
        }
    }
}
