package com.example.user.login.security;

import com.example.user.login.jwt.filter.JWTFilter;
import com.example.user.login.jwt.filter.CustomLogoutFilter;
import com.example.user.login.jwt.util.CookieUtil;
import com.example.user.login.jwt.util.JWTUtil;
import com.example.user.login.jwt.filter.LoginFilter;
import com.example.user.login.repository.RefreshRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshRepository refreshRepository;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, CookieUtil cookieUtil, RefreshRepository refreshRepository) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.refreshRepository = refreshRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        http
                .csrf((auth) -> auth.disable());

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/member/**", "/css/**", "/js/**", "/image/**", "/uploads/**").permitAll()  // 로그인 페이지와 회원가입 요청은 모두 허용
                        .requestMatchers("/reissue").permitAll()
                        .requestMatchers("/basic/items").permitAll()
                        .requestMatchers("/basic/items/images/**").permitAll() // 상품 이미지
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/basic/items/{itemID}")).permitAll()
                        .requestMatchers("/basic/items/add").hasRole("USER")
                        .requestMatchers("/basic/items/chat").hasRole("USER")
                        .requestMatchers("/basic/chatroom/**").hasRole("USER")
                        .requestMatchers("/basic/chat/enter/**").hasRole("USER")
                        .requestMatchers("/basic/mypage").hasRole("USER")
                        .requestMatchers("/basic/mypage/**").hasRole("USER")
                        .requestMatchers("/basic/mypage/update").hasRole("USER")
                        .requestMatchers("/chat/enter/**").hasRole("USER")
                        .requestMatchers("/basic/chatroom-selection/**").hasRole("USER")
                        .requestMatchers("/ws/chat").permitAll()
                        .requestMatchers("/chatroom/**").permitAll()
                        .requestMatchers("/member/check-login").permitAll()  // 추가
                        .anyRequest().authenticated()
                );

        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, cookieUtil, refreshRepository), UsernamePasswordAuthenticationFilter.class);

        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }
}