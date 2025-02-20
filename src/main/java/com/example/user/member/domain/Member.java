package com.example.user.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id")
    private String loginId;
    
    private String password;
    
    private String name;
    
    private String email;
    
    private String role;

    public Member encodePassword(PasswordEncoder passwordEncoder) {
        return new Member(this.loginId, passwordEncoder.encode(this.password), this.name, this.email, this.role);
    }

    // 🔹 필요한 생성자 추가 (빌더 패턴과 호환)
    @Builder
    public Member(String loginId, String password, String name, String email, String role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = (role != null) ? role : "ROLE_USER";
    }
}
