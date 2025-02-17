package com.example.user.login.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/member/add", "/member/check-login-id", "/member/check-email", "/css/**", "/js/**", "/images/**").permitAll()  // 로그인 페이지와 회원가입 요청은 모두 허용
                        .requestMatchers("/basic/items/**").hasRole("USER")
                        .anyRequest().authenticated()
                );
        http
                .formLogin((auth) -> auth
                        .loginPage("/login")  // 로그인 페이지 URL 지정
                        .loginProcessingUrl("/loginProc")
                        .permitAll()
                        .defaultSuccessUrl("/basic/items", true) // 로그인 성공 시 이동할 페이지
                        .failureUrl("/login?error=true") // 로그인 실패 시 이동할 페이지
                        .permitAll()
                );
//       http
//               .csrf((auth) -> auth.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
