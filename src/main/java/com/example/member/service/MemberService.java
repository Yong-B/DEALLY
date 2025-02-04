package com.example.member.service;

import com.example.member.domain.Member;
import com.example.member.repository.MemberRepository;
import com.example.member.usecase.MemberSaveUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberSaveUseCase {
    private final MemberRepository memberRepository;

    @Override
    public Member save(Member member) {
        return memberRepository.save(member);
    }
    
}
