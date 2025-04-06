package com.example.user.login.service;

import com.example.user.login.domain.RefreshToken;
import com.example.user.login.jwt.util.CookieUtil;
import com.example.user.login.jwt.util.JWTUtil;
import com.example.user.login.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReissueService {

    private final JWTUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshRepository refreshRepository;

    public ReissueService(JWTUtil jwtUtil, CookieUtil cookieUtil, RefreshRepository refreshRepository) {

        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.refreshRepository = refreshRepository;
    }



    public ResponseEntity<?> reissueToken(HttpServletRequest request, HttpServletResponse response) {

        // get refresh token
        String refresh = extractRefreshToken(request);
        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // 새로운 액세스 토큰 발급
        String loginId = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);
        String newAccess = jwtUtil.createJwt("access", loginId, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", loginId, role, 86400000L);

        refreshRepository.deleteByRefresh(refresh);
        addRefreshToken(loginId, refresh, 86400000L);

        // response 헤더에 추가
        response.setHeader("access", newAccess);
        response.addCookie(cookieUtil.createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addRefreshToken(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshToken = RefreshToken.builder()
                .loginId(username)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refreshToken);
    }


    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}