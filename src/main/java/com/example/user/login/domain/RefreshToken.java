package com.example.user.login.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "refreshtoken")
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String refresh;
    private String expiration;

    @Builder
    public RefreshToken(String loginId, String refresh, String expiration) {
        this.loginId = loginId;
        this.refresh = refresh;
        this.expiration = expiration;
    }
}
