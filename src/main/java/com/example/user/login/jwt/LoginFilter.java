package com.example.user.login.jwt;

import com.example.user.login.security.dto.CustomMemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        setUsernameParameter("loginId");
        String loginId = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println(loginId);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password, null);

        return authenticationManager.authenticate(authToken);
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();
        String loginId = customMemberDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(loginId, role, 60 * 60 * 1000L);
        
        response.addHeader("Authorization", "Bearer " + token);
     
//        response.sendRedirect("/basic/items");
//       
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        System.out.println("fail");
        response.sendRedirect("/login");
    }
}
