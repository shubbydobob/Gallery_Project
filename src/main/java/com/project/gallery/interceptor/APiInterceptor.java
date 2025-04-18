package com.project.gallery.interceptor;

import com.project.gallery.account.helper.AccountHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class APiInterceptor implements HandlerInterceptor {

    private final AccountHelper accountHelper;

    public APiInterceptor(AccountHelper accountHelper) {
        this.accountHelper = accountHelper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if(accountHelper.getMemberId(request) == null) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
