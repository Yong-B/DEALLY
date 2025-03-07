package com.example.user.login.jwt.filter;

import com.example.user.login.jwt.util.JWTUtil;
import com.example.user.login.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilter {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public CustomLogoutFilter(JWTUtil jwtUtil, RefreshRepository refreshRepository) {

        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }
    
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException,ServletException {

        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        //refresh null check
        if (refresh == null) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        refreshRepository.deleteByRefresh(refresh);

        // Refresh 토큰 Cookie 삭제
        Cookie refreshCookie = new Cookie("refresh", null);
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);  // ✅ 클라이언트에서 접근 못하게 설정
        refreshCookie.setSecure(true);    // ✅ HTTPS에서만 전송 (개발 환경에 따라 변경)

// Access 토큰 Cookie 삭제
        Cookie accessCookie = new Cookie("access", null);
        accessCookie.setMaxAge(0);
        accessCookie.setPath("/");
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);

// 쿠키 삭제 적용
        response.addCookie(refreshCookie);
        response.addCookie(accessCookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}