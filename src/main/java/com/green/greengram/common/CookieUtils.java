package com.green.greengram.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CookieUtils {
    // req header 에서 내가 원하는 쿠키를 찾는 메서드
    public Cookie getCookie(HttpServletRequest req , String name){
        // req 에 모든 http 요청이 담겨있음 , 원하는 타입으로 맞춰서 보냄
        Cookie[] cookies = req.getCookies();
        if (cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals(name)){
                    return cookie;
                }
            }
        }
        return null;
    }
    // RES header 에 내가 원하는 쿠키를 담는 메소드
    public void setCookie(HttpServletResponse res, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name , value);
        cookie.setPath("/api/user/access-token"); // 이 요청으로 들어올 때만 쿠키값이 넘어올 수 있도록
        cookie.setHttpOnly(true); // 보안 쿠키 설정, 프론트에서 js 로 쿠키값을 얻을 수 있다.
        // 없으면 쿠키에서 값을 못 얻음
        cookie.setMaxAge(maxAge);
        res.addCookie(cookie);
    }

}
