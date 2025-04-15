package com.example.mypage.service;

import com.example.product.domain.Item;
import com.example.product.repository.ItemRepository;
import com.example.user.member.controller.dto.MemberInfoDto;
import com.example.user.member.domain.Member;
import com.example.user.member.repository.MemberRepository;
import com.example.user.member.usecase.MemberUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MypageService implements MemberUpdateUseCase {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ItemRepository itemRepository;
    
    public MemberInfoDto getMemberInfo(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        return new MemberInfoDto(
                member.getLoginId(),
                member.getEmail(),
                member.getName()
        );
    }
    
    public Member findByLoginId(String loginId) {
        System.out.println("Looking for member with loginId: " + loginId);
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with loginId:" + loginId));
    }

    @Override
    public Member updatePassword(String loginId, String newPassword) {
        Member findMember = findByLoginId(loginId);
        String encodedPassword = passwordEncoder.encode(newPassword);
        findMember.updatePassword(encodedPassword);
        return memberRepository.save(findMember);
    }
}
