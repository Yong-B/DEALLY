package com.example.user.member.service;

import com.example.user.member.domain.Member;
import com.example.user.member.repository.MemberRepository;
import com.example.user.member.usecase.MemberFindUseCase;
import com.example.user.member.usecase.MemberSaveUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberSaveUseCase, MemberFindUseCase {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isLoginIdDuplicate(String loginId) {
        return memberRepository.findByLoginId(loginId).isPresent();
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Override
    public Member save(Member member) {
        // 로그인 ID 중복 검사
        if (isLoginIdDuplicate(member.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 로그인 ID입니다.");
        }
        // 이메일 중복 검사
        if (isEmailDuplicate(member.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
       
        Member encodedMember = member.encodePassword(passwordEncoder);
        
        return memberRepository.save(encodedMember);
    }

    @Override
    public Member findByLoginId(String loginId) {
        System.out.println("Looking for member with loginId: " + loginId);
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with loginId:" + loginId));
    }

}