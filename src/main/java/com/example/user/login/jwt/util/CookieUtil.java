package com.example.user.login.jwt.util;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public Cookie createCookie(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);  // JavaScript에서 접근 불가능 (보안 강화)
        cookie.setSecure(false);   // HTTPS가 아니라면 false (운영 환경에서는 true로 설정)
        cookie.setPath("/");       // 모든 경로에서 쿠키 사용 가능
        cookie.setMaxAge(24 * 60 * 60); // 1일 동안 유효

        return cookie;
    }
}
