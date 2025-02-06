package com.example.user.login.usecase;

import com.example.user.member.domain.Member;

import java.util.Optional;

public interface LoginUseCase {
    Optional<Member> login(String loginId, String password);
}
