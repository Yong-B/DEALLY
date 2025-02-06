package com.example.user.member.service;

import com.example.user.member.domain.Member;
import com.example.user.member.repository.MemberRepository;
import com.example.user.member.usecase.MemberFindUseCase;
import com.example.user.member.usecase.MemberSaveUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberSaveUseCase, MemberFindUseCase {
    private final MemberRepository memberRepository;

    @Override
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }
}
