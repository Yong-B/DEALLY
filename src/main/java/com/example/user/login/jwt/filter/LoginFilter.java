package com.example.user.login.jwt.filter;

import com.example.user.login.domain.RefreshToken;
import com.example.user.login.jwt.util.CookieUtil;
import com.example.user.login.jwt.util.JWTUtil;
import com.example.user.login.repository.RefreshRepository;
import com.example.user.login.security.dto.CustomMemberDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshRepository refreshRepository;
    
    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, CookieUtil cookieUtil, RefreshRepository refreshRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            // JSON 데이터를 파싱하기 위해 ObjectMapper 사용
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> loginData = objectMapper.readValue(request.getInputStream(), Map.class);

            String loginId = loginData.get("loginId"); // 로그인 ID 가져오기
            String password = loginData.get("password"); // 비밀번호 가져오기

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginId, password);

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("로그인 요청을 처리하는 중 오류 발생", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();
        String loginId = customMemberDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // JWT 생성
        String access = jwtUtil.createJwt("access", loginId, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", loginId, role, 86400000L);
        
        //Refresh 토큰 저장
        addRefreshToken(loginId, refresh, 86400000L);

        // 응답 헤더 설정
        response.setHeader("access", access);
        response.addCookie(cookieUtil.createCookie("refresh", refresh));
        response.addCookie(cookieUtil.createCookie("access", access));
        response.setStatus(HttpStatus.OK.value());
        
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
       response.setStatus(403);
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
}
