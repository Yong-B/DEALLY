package com.example.user.login.jwt.filter;

import com.example.user.login.jwt.util.JWTUtil;
import com.example.user.login.security.dto.CustomMemberDetails;
import com.example.user.member.domain.Member;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getTokenFromCookie(request);

        if (accessToken == null) {
            System.out.println("No access token");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 만료 여부 확인 (만료 시, 다음 필터로 넘김 - 리프레시 토큰 처리 가능)
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            System.out.println("Access token expired. Proceeding to next filter.");
            filterChain.doFilter(request, response); // 리프레시 토큰 필터에서 처리 가능하도록 넘김
            return;
        }

        //  토큰이 access인지 확인
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            System.out.println("Invalid access token category: " + category);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //  토큰에서 사용자 정보 추출
        String loginId = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        System.out.println("User authenticated: " + loginId + " with role: " + role);

        //  SecurityContextHolder에 인증 정보 설정
        Member member = Member.builder()
                .loginId(loginId)
                .password("temppassword") // 비밀번호는 사용하지 않음
                .role(role)
                .build();

        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        System.out.println("SecurityContext updated with user: " + loginId);    

        filterChain.doFilter(request, response);
        
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("access".equals(cookie.getName())) {
                return cookie.getValue(); // JWT 값 반환
            }
        }
        return null;
    }
}
