package com.example.user.login.security.service;

import com.example.user.member.domain.Member;
import com.example.user.member.usecase.MemberFindUseCase;
import com.example.user.login.security.dto.CustomMemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberFindUseCase memberFindUseCase;  // memberFindUseCase 주입

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        // loginId로 Member를 조회
        Member member = memberFindUseCase.findByLoginId(loginId);
        
//        log.info("로그인 시도 - loginId: {}, member: {}", loginId, member);
        // Member가 없을 경우, 예외 던지기
        if (member == null) {
//            log.error("사용자를 찾을 수 없습니다. 로그인 ID: {}", loginId);
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다: " + loginId);
        }

        // 회원이 존재하면 CustomMemberDetails 반환
        return new CustomMemberDetails(member); 
    }
}

    