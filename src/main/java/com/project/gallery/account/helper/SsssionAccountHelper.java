package com.project.gallery.account.helper;

import com.project.gallery.account.dto.AccountJoinRequest;
import com.project.gallery.account.dto.AccountLoginRequest;
import com.project.gallery.account.etc.AccountConstants;
import com.project.gallery.common.util.HttpUtils;
import com.project.gallery.member.entity.Member;
import com.project.gallery.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsssionAccountHelper implements AccountHelper {

    private final MemberService memberService;

    @Override
    public void join(AccountJoinRequest joinReq) {
        memberService.save(joinReq.getName(), joinReq.getLoginId(), joinReq.getLoginPw());
    }

    @Override
    public String login(AccountLoginRequest loginReq, jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse res) {
        Member member = memberService.find(loginReq.getLoginId(), loginReq.getLoginPw());

        if (member == null) {
            return null;
        }

        HttpUtils.setSession(req, AccountConstants.MEMBER_ID_NAME, member.getId());
        return member.getId().toString();
    }

    @Override
    public Integer getMemberId(jakarta.servlet.http.HttpServletRequest req) {
        Object memberId = HttpUtils.getSession(req, AccountConstants.MEMBER_ID_NAME);

        if (memberId != null) {
            return (int) memberId;
        }

        return null;
    }

    @Override
    public boolean isLoggedIn(HttpServletRequest req) {
        return getMemberId(req) != null;
    }

    @Override
    public void logout(HttpServletRequest req, HttpServletResponse res) {
        HttpUtils.removeSession(req, AccountConstants.MEMBER_ID_NAME);
    }
}
