package com.project.gallery.account.helper;

import com.project.gallery.account.dto.AccountJoinRequest;
import com.project.gallery.account.dto.AccountLoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AccountHelper {

    void join(AccountJoinRequest joinReq);

    String login(AccountLoginRequest loginReq, HttpServletRequest req, HttpServletResponse res) ;

    Integer getMemberId(HttpServletRequest req);

    boolean isLoggedIn(HttpServletRequest req);

    void logout(HttpServletRequest req, HttpServletResponse res);
}
