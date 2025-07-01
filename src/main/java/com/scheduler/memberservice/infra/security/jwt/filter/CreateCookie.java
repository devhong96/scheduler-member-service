package com.scheduler.memberservice.infra.security.jwt.filter;

import jakarta.servlet.http.Cookie;

public class CreateCookie {

    public static Cookie createCookie(String value) {
        Cookie cookie = new Cookie("refresh", value);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
