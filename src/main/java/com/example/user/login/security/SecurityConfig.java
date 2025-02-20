package com.example.user.login.security;

import com.example.user.login.jwt.JWTFilter;
import com.example.user.login.jwt.JWTUtil;
import com.example.user.login.jwt.LoginFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
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
                        .requestMatchers("/", "/login", "/member/add", "/member/check-login-id", "/member/check-email", "/css/**", "/js/**", "/images/**").permitAll()  // 로그인 페이지와 회원가입 요청은 모두 허용
                        .requestMatchers("/basic/items").hasRole("USER")
                        .anyRequest().authenticated()
                );

        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
        
        
        
//        http
//                .formLogin((auth) -> auth
//                        .loginPage("/login")  // 로그인 페이지 URL 지정
//                        .loginProcessingUrl("/loginProc")
//                        .permitAll()
//                        .defaultSuccessUrl("/basic/items", true) // 로그인 성공 시 이동할 페이지
//                        .failureUrl("/login?error=true") // 로그인 실패 시 이동할 페이지
//                        .permitAll()
//                );

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
