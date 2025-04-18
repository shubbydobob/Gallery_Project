package com.project.gallery.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpUtils {

    // 세션 입력
    public static void setSession(HttpServletRequest req, String key, Object value ) {
        req.getSession().setAttribute(key, value);
    }

    // 세션 값 조회
    public static Object getSession(HttpServletRequest req, String key) {
        return req.getSession().getAttribute(key);
    }

    // 세션 삭제
    public static void removeSession(HttpServletRequest req, String key) {
        req.getSession().removeAttribute(key);
    }

    // 쿠키 입력
    public static void setCookie(HttpServletResponse response, String key, String value, int expSeconds) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        if(expSeconds > 0) {
            cookie.setMaxAge(expSeconds);
        } else {
            response.addCookie(cookie);
        }
    }
    // 쿠키 값 조회
    public static String getCookieValue(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    
    // 쿠키 삭제
    public static void removeCookie(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    
    // 토큰 조회
    public static String getBearerToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token != null) {
            return token.replace("Bearer ", "").trim(); // Bearer 값 조회
        }
        return null;
    }
}
