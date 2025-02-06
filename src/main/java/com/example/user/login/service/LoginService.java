package com.example.user.login.service;

import com.example.user.login.usecase.LoginUseCase;
import com.example.user.member.domain.Member;
import com.example.user.member.usecase.MemberFindUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase{
    private final MemberFindUseCase memberFindUseCase;

    @Override
    public Optional<Member> login(String loginId, String password) {
        return memberFindUseCase.findByLoginId(loginId)
                .filter(member -> member.getPassword().equals(password));
    }
}
